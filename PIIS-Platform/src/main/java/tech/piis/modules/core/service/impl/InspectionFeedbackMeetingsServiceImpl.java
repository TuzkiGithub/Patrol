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
import tech.piis.modules.core.domain.po.InspectionFeedbackMeetingsPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionFeedbackMeetingsMapper;
import tech.piis.modules.core.service.IInspectionFeedbackMeetingsService;
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
 * 反馈会Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@Transactional
@Slf4j
@Service
public class InspectionFeedbackMeetingsServiceImpl implements IInspectionFeedbackMeetingsService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionFeedbackMeetingsMapper inspectionFeedbackMeetingsMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String BIZ_NAME = "FeedbackMeetings";

    /**
     * 统计巡视方案下被巡视单位InspectionFeedbackMeetings次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionFeedbackMeetingsCount(String planId) throws BaseException {
        return inspectionFeedbackMeetingsMapper.selectInspectionFeedbackMeetingsCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询反馈会列表
     *
     * @param inspectionFeedbackMeetings
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionFeedbackMeetingsPO> selectInspectionFeedbackMeetingsList(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException {
        QueryWrapper<InspectionFeedbackMeetingsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionFeedbackMeetings.getUnitsId());
        queryWrapper.eq("plan_id", inspectionFeedbackMeetings.getPlanId());
        List<InspectionFeedbackMeetingsPO> inspectionFeedbackMeetingsList = inspectionFeedbackMeetingsMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionFeedbackMeetingsList)) {
            inspectionFeedbackMeetingsList.forEach(var -> var.setDocuments(documentService.getFileListByBizId(BIZ_NAME + var.getFeedbackMeetingsId())));
        }
        return inspectionFeedbackMeetingsList;
    }

    /**
     * 新增反馈会
     *
     * @param inspectionFeedbackMeetings
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException {
        int result = inspectionFeedbackMeetingsMapper.insert(inspectionFeedbackMeetings);
        List<PiisDocumentPO> documents = inspectionFeedbackMeetings.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionFeedbackMeetings.getFeedbackMeetingsId();
        documentService.updateDocumentBatch(documents, BIZ_NAME + bizId, FileEnum.FEEDBACK_MEETINGS_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改反馈会
     *
     * @param inspectionFeedbackMeetings
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException {
        Object bizId = inspectionFeedbackMeetings.getFeedbackMeetingsId();
        documentService.updateDocumentBatch(inspectionFeedbackMeetings.getDocuments(), BIZ_NAME + bizId, FileEnum.FEEDBACK_MEETINGS_FILE.getCode());
        return inspectionFeedbackMeetingsMapper.updateById(inspectionFeedbackMeetings);
    }

    /**
     * 根据ID批量删除反馈会
     *
     * @param feedbackMeetingsIds 反馈会编号
     * @return
     */
    @Override
    public int deleteByInspectionFeedbackMeetingsIds(Long[] feedbackMeetingsIds) throws BaseException {
        List<Long> list = Arrays.asList(feedbackMeetingsIds);
        return inspectionFeedbackMeetingsMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionFeedbackMeetings
     */
    private void handleTodo(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) {
        String planId = inspectionFeedbackMeetings.getPlanId();
        Long unitsId = inspectionFeedbackMeetings.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[反馈会]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionFeedbackMeetings.getFeedbackMeetingsId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionFeedbackMeetings.getApproverId())
                .setApproverName(inspectionFeedbackMeetings.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionFeedbackMeetingsPO> inspectionFeedbackMeetingsList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionFeedbackMeetingsList)) {
            inspectionFeedbackMeetingsList.forEach(inspectionFeedbackMeetings -> {
                inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionFeedbackMeetingsMapper.updateById(inspectionFeedbackMeetings.setDocuments(null));
                handleTodo(inspectionFeedbackMeetings);
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
        log.info("###ApplicationListener notify event [反馈会]###");
        Object object = event.getSource();
        if (object instanceof InspectionFeedbackMeetingsServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionFeedbackMeetingsPO inspectionFeedbackMeetings = new InspectionFeedbackMeetingsPO()
                    .setFeedbackMeetingsId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                event.setData(inspectionFeedbackMeetingsMapper.selectInspectionFeedbackMeetingsWithFileById(inspectionFeedbackMeetings));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionFeedbackMeetingsPO.class, inspectionFeedbackMeetings);
                inspectionFeedbackMeetingsMapper.updateById(inspectionFeedbackMeetings);
            }
        }
    }
}
