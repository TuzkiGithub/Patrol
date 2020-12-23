package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.BizUtils;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionPatrolReportPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionPatrolReportMapper;
import tech.piis.modules.core.service.IInspectionPatrolReportService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 巡视报告Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@Transactional
@Slf4j
@Service
public class InspectionPatrolReportServiceImpl implements IInspectionPatrolReportService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionPatrolReportMapper inspectionPatrolReportMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String BIZ_TIME = "PatrolReport";


    /**
     * 统计巡视方案下被巡视单位InspectionPatrolReport次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionPatrolReportCount(String planId) throws BaseException {
        return inspectionPatrolReportMapper.selectInspectionPatrolReportCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询巡视报告列表
     *
     * @param inspectionPatrolReport
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionPatrolReportPO> selectInspectionPatrolReportList(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException {
        QueryWrapper<InspectionPatrolReportPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionPatrolReport.getUnitsId());
        queryWrapper.eq("plan_id", inspectionPatrolReport.getPlanId());
        List<InspectionPatrolReportPO> inspectionPatrolReportList = inspectionPatrolReportMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionPatrolReportList)) {
            inspectionPatrolReportList.forEach(var -> {
                var.setForwardSendList(BizUtils.paramsCovert2List(var.getForwardSendIds(), var.getForwardSendNames()));
                var.setDocuments(documentService.getFileListByBizId(BIZ_TIME + var.getPatrolReportId()));
            });
        }
        return inspectionPatrolReportList;
    }

    /**
     * 新增巡视报告
     *
     * @param inspectionPatrolReport
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException {
        inspectionPatrolReport.setForwardSendIds(BizUtils.paramsCovert2String(inspectionPatrolReport.getForwardSendList()).get(0));
        inspectionPatrolReport.setForwardSendNames(BizUtils.paramsCovert2String(inspectionPatrolReport.getForwardSendList()).get(1));
        int result = inspectionPatrolReportMapper.insert(inspectionPatrolReport);
        List<PiisDocumentPO> documents = inspectionPatrolReport.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionPatrolReport.getPatrolReportId();
        documentService.updateDocumentBatch(documents, BIZ_TIME + bizId, FileEnum.PATROL_REPORT_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改巡视报告
     *
     * @param inspectionPatrolReport
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException {
        inspectionPatrolReport.setForwardSendIds(BizUtils.paramsCovert2String(inspectionPatrolReport.getForwardSendList()).get(0));
        inspectionPatrolReport.setForwardSendNames(BizUtils.paramsCovert2String(inspectionPatrolReport.getForwardSendList()).get(1));
        Object bizId = inspectionPatrolReport.getPatrolReportId();
        documentService.updateDocumentBatch(inspectionPatrolReport.getDocuments(), BIZ_TIME + bizId, FileEnum.PATROL_REPORT_FILE.getCode());
        return inspectionPatrolReportMapper.updateById(inspectionPatrolReport);
    }

    /**
     * 根据ID批量删除巡视报告
     *
     * @param patrolReportIds 巡视报告编号
     * @return
     */
    @Override
    public int deleteByInspectionPatrolReportIds(Long[] patrolReportIds) throws BaseException {
        List<Long> list = Arrays.asList(patrolReportIds);
        return inspectionPatrolReportMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionPatrolReport
     */
    private void handleTodo(InspectionPatrolReportPO inspectionPatrolReport) {
        String planId = inspectionPatrolReport.getPlanId();
        Long unitsId = inspectionPatrolReport.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[巡视报告]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionPatrolReport.getPatrolReportId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionPatrolReport.getApproverId())
                .setApproverName(inspectionPatrolReport.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionPatrolReportPO> inspectionPatrolReportList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionPatrolReportList)) {
            inspectionPatrolReportList.forEach(inspectionPatrolReport -> {
                inspectionPatrolReport.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionPatrolReportMapper.updateById(inspectionPatrolReport.setDocuments(null));
                handleTodo(inspectionPatrolReport);
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
        log.info("###ApplicationListener notify event [巡视报告]###");
        Object object = event.getSource();
        if (object instanceof InspectionPatrolReportServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionPatrolReportPO inspectionPatrolReport = new InspectionPatrolReportPO()
                    .setPatrolReportId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionPatrolReportPO result = inspectionPatrolReportMapper.selectInspectionPatrolReportWithFileById(inspectionPatrolReport);
                result.setForwardSendList(BizUtils.paramsCovert2List(result.getForwardSendIds(), result.getReportName()));
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionPatrolReport.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionPatrolReport.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionPatrolReport.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionPatrolReportPO.class, inspectionPatrolReport);
                inspectionPatrolReportMapper.updateById(inspectionPatrolReport);
            }
        }
    }
}
