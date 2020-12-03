package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionVisitPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionVisitMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IInspectionVisitService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 来访Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
@Slf4j
public class InspectionVisitServiceImpl implements IInspectionVisitService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionVisitMapper inspectionVisitMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 与前端接口定义文件类型
     */
    private static final Map<Long, Long> TO_DICT_MAP = new HashMap<>();
    private static final Map<Long, Long> TO_TEMP_MAP = new HashMap<>();

    static {
        TO_DICT_MAP.put(1L, FileEnum.VISIT_OTHER_FILE.getCode());
        TO_DICT_MAP.put(0L, FileEnum.VISIT_SEAL_FILE.getCode());
    }

    static {
        TO_TEMP_MAP.put(FileEnum.VISIT_OTHER_FILE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.VISIT_SEAL_FILE.getCode(), 0L);
    }

    /**
     * 统计巡视方案下被巡视单位InspectionVisit次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionVisitCount(String planId) throws BaseException {
        return inspectionVisitMapper.selectInspectionVisitCount(planId);
    }

    /**
     * 查询来访列表
     *
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    public List<InspectionVisitPO> selectInspectionVisitList(InspectionVisitPO inspectionVisit) throws BaseException {
        QueryWrapper<InspectionVisitPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionVisit.getUnitsId());
        queryWrapper.eq("plan_id", inspectionVisit.getPlanId());
        queryWrapper.orderByDesc("created_time");
        return inspectionVisitMapper.selectList(queryWrapper);
    }

    /**
     * 新增来访
     *
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    public int save(InspectionVisitPO inspectionVisit) throws BaseException {
        List<PiisDocumentPO> documents = inspectionVisit.getDocuments();
        int result = inspectionVisitMapper.insert(inspectionVisit.setDocuments(null));
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                documentService.updateDocumentById(document.setObjectId("Visit" + inspectionVisit.getVisitId()));
            }
        }
        return result;
    }

    /**
     * 根据ID修改来访
     *
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    public int update(InspectionVisitPO inspectionVisit) throws BaseException {
        List<PiisDocumentPO> documents = inspectionVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setObjectId("Visit" + inspectionVisit.getVisitId());
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
        return inspectionVisitMapper.updateById(inspectionVisit.setDocuments(null));
    }

    /**
     * 根据ID批量删除来访
     *
     * @param callVisitIds 来访编号
     * @return
     */
    public int deleteByInspectionVisitIds(String[] callVisitIds) {
        List<String> list = Arrays.asList(callVisitIds);
        return inspectionVisitMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param visitPO
     */
    private void handleTodo(InspectionVisitPO visitPO) {
        String planId = visitPO.getPlanId();
        Long unitsId = visitPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[信访管理-来访]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(visitPO.getVisitId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(visitPO.getApproverId())
                .setApproverName(visitPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批
     *
     * @param visitList
     */
    @Override
    public void doApprovals(List<InspectionVisitPO> visitList) {
        if (!CollectionUtils.isEmpty(visitList)) {
            visitList.forEach(visit -> {
                visit.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionVisitMapper.updateById(visit.setDocuments(null));
                handleTodo(visit);
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
        System.out.println("###ApplicationListener notify event [信访管理-来访]###");
        log.info("###ApplicationListener notify event [信访管理-来访]###");
        Object object = event.getSource();
        if (object instanceof InspectionVisitServiceImpl) {
            Integer eventType = event.getEventType();
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionVisitPO params = new InspectionVisitPO()
                        .setVisitId(Long.valueOf(event.getBizId()));
                event.setData(selectVisitWithFile(params));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                InspectionVisitPO visitPO = new InspectionVisitPO()
                        .setVisitId(Long.valueOf(event.getBizId()));
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        visitPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        visitPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    visitPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionVisitPO.class, visitPO);
                inspectionVisitMapper.updateById(visitPO);
            }
        }
    }

    /**
     * 查询来访详细信息
     *
     * @param params
     * @return
     */
    private InspectionVisitPO selectVisitWithFile(InspectionVisitPO params) {
        InspectionVisitPO inspectionVisitPO = inspectionVisitMapper.selectVisitWithFile(params);
        BizUtils.convertFileDict(inspectionVisitPO.getDocuments(), TO_TEMP_MAP);
        return inspectionVisitPO;
    }
}
