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
import tech.piis.modules.core.domain.po.InspectionPresentMeetingPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionPresentMeetingMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IInspectionPresentMeetingService;
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
 * 列席会议 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-07
 */
@Transactional
@Slf4j
@Service
public class InspectionPresentMeetingServiceImpl implements IInspectionPresentMeetingService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionPresentMeetingMapper inspectionPresentMeetingMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    public static final String bizName = "PresentMeeting";

    /**
     * 统计巡视方案下被巡视单位InspectionPresentMeeting次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionPresentMeetingCount(String planId) throws BaseException {
        return inspectionPresentMeetingMapper.selectInspectionPresentMeetingCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询列席会议 列表
     *
     * @param inspectionPresentMeeting
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionPresentMeetingPO> selectInspectionPresentMeetingList(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException {
        QueryWrapper<InspectionPresentMeetingPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionPresentMeeting.getUnitsId());
        queryWrapper.eq("plan_id", inspectionPresentMeeting.getPlanId());
        queryWrapper.orderByDesc("created_time");
        List<InspectionPresentMeetingPO> inspectionPresentMeetingList = inspectionPresentMeetingMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionPresentMeetingList)) {
            inspectionPresentMeetingList.forEach(var -> var.setDocuments(documentService.getFileListByBizId(bizName + var.getPresentMeetingId())));
        }
        return inspectionPresentMeetingList;
    }

    /**
     * 新增列席会议
     *
     * @param inspectionPresentMeeting
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException {
        int result = inspectionPresentMeetingMapper.insert(inspectionPresentMeeting);
        List<PiisDocumentPO> documents = inspectionPresentMeeting.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionPresentMeeting.getPresentMeetingId();
        documentService.updateDocumentBatch(documents, bizName + bizId, FileEnum.PRESENT_OTHER_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改列席会议
     *
     * @param inspectionPresentMeeting
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException {
        Object bizId = inspectionPresentMeeting.getPresentMeetingId();
        documentService.updateDocumentBatch(inspectionPresentMeeting.getDocuments(), bizName + bizId, FileEnum.PRESENT_OTHER_FILE.getCode());
        return inspectionPresentMeetingMapper.updateById(inspectionPresentMeeting);
    }

    /**
     * 根据ID批量删除列席会议
     *
     * @param presentMeetingIds 列席会议 编号
     * @return
     */
    @Override
    public int deleteByInspectionPresentMeetingIds(Long[] presentMeetingIds) throws BaseException {
        List<Long> list = Arrays.asList(presentMeetingIds);
        return inspectionPresentMeetingMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionPresentMeeting
     */
    private void handleTodo(InspectionPresentMeetingPO inspectionPresentMeeting) {
        String planId = inspectionPresentMeeting.getPlanId();
        Long unitsId = inspectionPresentMeeting.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[列席会议]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionPresentMeeting.getPresentMeetingId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionPresentMeeting.getApproverId())
                .setApproverName(inspectionPresentMeeting.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionPresentMeetingPO> inspectionPresentMeetingList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionPresentMeetingList)) {
            inspectionPresentMeetingList.forEach(inspectionPresentMeeting -> {
                inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionPresentMeetingMapper.updateById(inspectionPresentMeeting.setDocuments(null));
                handleTodo(inspectionPresentMeeting);
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
        log.info("###ApplicationListener notify event [列席会议]###");
        Object object = event.getSource();
        if (object instanceof InspectionPresentMeetingServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionPresentMeetingPO inspectionPresentMeeting = new InspectionPresentMeetingPO()
                    .setPresentMeetingId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionPresentMeetingPO result = inspectionPresentMeetingMapper.selectInspectionPresentMeetingWithFileById(inspectionPresentMeeting);
                result.setReporter(BizUtils.paramsCovert2List(result.getReporterId(), result.getReporterName()));
                result.setParticipants(BizUtils.paramsCovert2List(result.getReportPersonId(), result.getReportPersonName()));
                result.setInspectionGroupPersons(BizUtils.paramsCovert2List(result.getInspectionGroupPersonId(), result.getInspectionGroupPersonName()));
                event.setData(result);
                event.setData(inspectionPresentMeetingMapper.selectInspectionPresentMeetingWithFileById(inspectionPresentMeeting));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionPresentMeetingPO.class, inspectionPresentMeeting);
                inspectionPresentMeetingMapper.updateById(inspectionPresentMeeting);
            }
        }
    }
}
