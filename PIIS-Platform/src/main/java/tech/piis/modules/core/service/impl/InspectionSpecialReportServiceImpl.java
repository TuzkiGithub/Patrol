package tech.piis.modules.core.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionSpecialReportPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionSpecialReportMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IInspectionSpecialReportService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * SPECIAL_REPORT Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-12
 */
@Service
@Slf4j
@Transactional
public class InspectionSpecialReportServiceImpl implements IInspectionSpecialReportService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionSpecialReportMapper inspectionSpecialReportMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String BIZ_NAME = "SpecialReport";

    /**
     * 统计巡视方案下被巡视单位的听取报告次数
     *
     * @param planId
     * @return
     */
    @Override
    public List<UnitsBizCountVO> selectSpecialReportCount(String planId) {
        return inspectionSpecialReportMapper.selectSpecialReportCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<InspectionSpecialReportPO> selectSpecialReport(InspectionSpecialReportPO inspectionSpecialReport) {
        return inspectionSpecialReportMapper.selectSpecialReportListWithFile(inspectionSpecialReport);
    }

    @Override
    public int update(InspectionSpecialReportPO inspectionSpecialReport) throws BaseException {
        List<PiisDocumentPO> documents = inspectionSpecialReport.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setFileDictId(FileEnum.SPECIAL_REPORT_FILE.getCode())
                                    .setObjectId(BIZ_NAME + inspectionSpecialReport.getSpecialReportId());
                            documentService.updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            documentService.deleteDocumentById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return inspectionSpecialReportMapper.updateById(inspectionSpecialReport);
    }

    @Override
    public int save(InspectionSpecialReportPO inspectionSpecialReport) throws BaseException {
        int result = inspectionSpecialReportMapper.insert(inspectionSpecialReport);
        List<PiisDocumentPO> documents = inspectionSpecialReport.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(documentPO -> documentPO.setOperationType(INSERT));
        }
        documentService.updateDocumentBatch(documents, BIZ_NAME + inspectionSpecialReport.getSpecialReportId(), FileEnum.SPECIAL_REPORT_FILE.getCode());
        return result;
    }

    /**
     * 新增代办
     *
     * @param specialReportPO
     */
    private void handleTodo(InspectionSpecialReportPO specialReportPO) {
        String planId = specialReportPO.getPlanId();
        Long unitsId = specialReportPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[听取汇报]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(specialReportPO.getSpecialReportId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(specialReportPO.getApproverId())
                .setApproverName(specialReportPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批专题报告
     *
     * @param inspectionSpecialReportPOList
     * @return
     */
    @Override
    public void doApprovals(List<InspectionSpecialReportPO> inspectionSpecialReportPOList) {
        if (!CollectionUtils.isEmpty(inspectionSpecialReportPOList)) {
            inspectionSpecialReportPOList.forEach(specialReportPO -> {
                specialReportPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionSpecialReportMapper.updateById(specialReportPO.setDocuments(null));
                handleTodo(specialReportPO);
            });
        }
    }

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(WorkFlowEvent event) {
        log.info("###ApplicationListener notify event [听取汇报]###");
        Object object = event.getSource();
        if (object instanceof InspectionSpecialReportServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionSpecialReportPO specialReportPO = new InspectionSpecialReportPO()
                    .setSpecialReportId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                event.setData(inspectionSpecialReportMapper.selectSpecialReportWithFile(specialReportPO));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        specialReportPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        specialReportPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    specialReportPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionSpecialReportPO.class, specialReportPO);
                inspectionSpecialReportMapper.updateById(specialReportPO);
            }
        }
    }

    @Override
    public int deleteBySpecialReportIds(Long[] specialReportIds) {
        List<Long> list = Arrays.asList(specialReportIds);
        return inspectionSpecialReportMapper.deleteBatchIds(list);
    }

}
