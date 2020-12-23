package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import tech.piis.modules.core.domain.po.InspectionFeedbackQuestionsPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionFeedbackMapper;
import tech.piis.modules.core.mapper.InspectionFeedbackQuestionsMapper;
import tech.piis.modules.core.service.IInspectionFeedbackService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.*;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 反馈意见 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Slf4j
@Transactional
@Service
public class InspectionFeedbackServiceImpl implements IInspectionFeedbackService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionFeedbackMapper inspectionFeedbackMapper;

    @Autowired
    private InspectionFeedbackQuestionsMapper inspectionFeedbackQuestionsMapper;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    /**
     * 统计巡视方案下被巡视单位InspectionFeedback次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionFeedbackCount(String planId) throws BaseException {
        return inspectionFeedbackMapper.selectInspectionFeedbackCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询反馈意见 列表
     *
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionFeedbackPO> selectInspectionFeedbackList(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        return inspectionFeedbackMapper.selectInspectionFeedbackList(inspectionFeedback);
    }

    /**
     * 新增反馈意见
     *
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        //设置主键
        String feedbackId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionFeedback.setFeedbackId(feedbackId);

        //新增问题清单
        List<InspectionFeedbackQuestionsPO> feedbackQuestionsList = inspectionFeedback.getFeedbackQuestionsList();
        if (!CollectionUtils.isEmpty(feedbackQuestionsList)) {
            feedbackQuestionsList.forEach(var -> {
                var.setFeedbackId(feedbackId);
                inspectionFeedbackQuestionsMapper.insert(var);
            });
        }
        return inspectionFeedbackMapper.insert(inspectionFeedback.setFeedbackQuestionsList(null));
    }

    /**
     * 根据ID修改反馈意见
     *
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        //修改问题清单

        List<InspectionFeedbackQuestionsPO> feedbackQuestionsList = inspectionFeedback.getFeedbackQuestionsList();
        if (!CollectionUtils.isEmpty(feedbackQuestionsList)) {
            feedbackQuestionsList.forEach(feedbackQuestion -> {
                feedbackQuestion.setFeedbackId(inspectionFeedback.getFeedbackId());
                Integer operationType = feedbackQuestion.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            inspectionFeedbackQuestionsMapper.insert(feedbackQuestion);
                            break;
                        case UPDATE:
                            inspectionFeedbackQuestionsMapper.updateById(feedbackQuestion);
                            break;
                        case DELETE:
                            inspectionFeedbackQuestionsMapper.deleteById(feedbackQuestion.getFeedbackQuestionsId());
                            break;
                    }
                }
            });

        }
        return inspectionFeedbackMapper.updateById(inspectionFeedback.setFeedbackQuestionsList(null));
    }

    /**
     * 根据ID批量删除反馈意见
     *
     * @param feedbackIds 反馈意见 编号
     * @return
     */
    @Override
    public int deleteByInspectionFeedbackIds(String[] feedbackIds) {
        List<String> list = Arrays.asList(feedbackIds);
        for (String feedbackId : feedbackIds) {
            QueryWrapper<InspectionFeedbackQuestionsPO> questionsPOQueryWrapper = new QueryWrapper<>();
            questionsPOQueryWrapper.eq("FEEDBACK_ID", feedbackId);
            inspectionFeedbackQuestionsMapper.delete(questionsPOQueryWrapper);
        }
        return inspectionFeedbackMapper.deleteBatchIds(list);
    }

    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(InspectionFeedbackPO inspectionFeedback) {
        QueryWrapper<InspectionFeedbackPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionFeedback.getUnitsId());
        queryWrapper.eq("plan_id", inspectionFeedback.getPlanId());
        return inspectionFeedbackMapper.selectCount(queryWrapper);
    }

    /**
     * 新增代办
     *
     * @param inspectionFeedback
     */
    private void handleTodo(InspectionFeedbackPO inspectionFeedback) {
        String planId = inspectionFeedback.getPlanId();
        Long unitsId = inspectionFeedback.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[反馈意见]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionFeedback.getFeedbackId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionFeedback.getApproverId())
                .setApproverName(inspectionFeedback.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批反馈意见
     *
     * @param inspectionFeedbackList
     */
    @Override
    public void doApprovals(List<InspectionFeedbackPO> inspectionFeedbackList) {
        if (!CollectionUtils.isEmpty(inspectionFeedbackList)) {
            inspectionFeedbackList.forEach(feedbackPO -> {
                feedbackPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionFeedbackMapper.updateById(feedbackPO);
                handleTodo(feedbackPO);
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
        log.info("###ApplicationListener notify event [反馈意见]###");
        Object object = event.getSource();
        if (object instanceof InspectionFeedbackServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionFeedbackPO feedbackPO = new InspectionFeedbackPO()
                    .setFeedbackId(event.getBizId());
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionFeedbackPO result = inspectionFeedbackMapper.selectInspectionFeedback(feedbackPO);
                result.setCopyUnitsList(BizUtils.paramsCovert2OrgList(result.getCopyCompanyId(), result.getCopyCompanyName()));
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        feedbackPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        feedbackPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    feedbackPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionFeedbackPO.class, feedbackPO);
                inspectionFeedbackMapper.updateById(feedbackPO);
            }
        }
    }
}
