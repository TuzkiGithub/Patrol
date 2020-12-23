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
import tech.piis.modules.core.domain.po.InspectionCallVisitPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionCallVisitMapper;
import tech.piis.modules.core.service.IInspectionCallVisitService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.*;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 来电Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
@Slf4j
public class InspectionCallVisitServiceImpl implements IInspectionCallVisitService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionCallVisitMapper inspectionCallVisitMapper;

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
        TO_DICT_MAP.put(1L, FileEnum.CALL_OTHER_FILE.getCode());
        TO_DICT_MAP.put(0L, FileEnum.CALL_SEAL_FILE.getCode());
    }

    static {
        TO_TEMP_MAP.put(FileEnum.CALL_OTHER_FILE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.CALL_SEAL_FILE.getCode(), 0L);
    }

    /**
     * 统计巡视方案下被巡视单位InspectionCallVisit次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionCallVisitCount(String planId) throws BaseException {
        return inspectionCallVisitMapper.selectInspectionCallVisitCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询来电列表
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    public List<InspectionCallVisitPO> selectInspectionCallVisitList(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        QueryWrapper<InspectionCallVisitPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionCallVisit.getUnitsId());
        queryWrapper.eq("plan_id", inspectionCallVisit.getPlanId());
        queryWrapper.orderByDesc("created_time");
        return inspectionCallVisitMapper.selectList(queryWrapper);
    }

    /**
     * 新增来电
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    public int save(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        List<PiisDocumentPO> documents = inspectionCallVisit.getDocuments();
        int result = inspectionCallVisitMapper.insert(inspectionCallVisit.setDocuments(null));
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                documentService.updateDocumentById(document.setObjectId("CallVisit" + inspectionCallVisit.getCallVisitId()));
            }
        }
        return result;
    }

    /**
     * 根据ID修改来电
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    public int update(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        List<PiisDocumentPO> documents = inspectionCallVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setObjectId("CallVisit" + inspectionCallVisit.getCallVisitId());
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
        return inspectionCallVisitMapper.updateById(inspectionCallVisit.setDocuments(null));
    }

    /**
     * 根据ID批量删除来电
     *
     * @param callVisitIds 来电编号
     * @return
     */
    public int deleteByInspectionCallVisitIds(String[] callVisitIds) {
        List<String> list = Arrays.asList(callVisitIds);
        return inspectionCallVisitMapper.deleteBatchIds(list);
    }

    /**
     * 新增代办
     *
     * @param inspectionCallVisit
     */
    private void handleTodo(InspectionCallVisitPO inspectionCallVisit) {
        String planId = inspectionCallVisit.getPlanId();
        Long unitsId = inspectionCallVisit.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[信访管理-来电]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionCallVisit.getCallVisitId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionCallVisit.getApproverId())
                .setApproverName(inspectionCallVisit.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }


    /**
     * 审批
     *
     * @param callVisitList
     */
    @Override
    public void doApprovals(List<InspectionCallVisitPO> callVisitList) {
        if (!CollectionUtils.isEmpty(callVisitList)) {
            callVisitList.forEach(callVisit -> {
                callVisit.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionCallVisitMapper.updateById(callVisit);
                handleTodo(callVisit);
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
        System.out.println("###ApplicationListener notify event [信访管理-来电]###");
        log.info("###ApplicationListener notify event [信访管理-来电]###");
        Object object = event.getSource();
        if (object instanceof InspectionCallVisitServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionCallVisitPO callVisitPO = new InspectionCallVisitPO()
                    .setCallVisitId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionCallVisitPO result = inspectionCallVisitMapper.selectVisitWithFile(callVisitPO);
                BizUtils.convertFileDict(result.getDocuments(), TO_TEMP_MAP);
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        callVisitPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        callVisitPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    callVisitPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionCallVisitPO.class, callVisitPO);
                inspectionCallVisitMapper.updateById(callVisitPO);
            }
        }
    }
}
