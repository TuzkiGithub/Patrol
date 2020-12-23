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
import tech.piis.modules.core.domain.po.InspectionMeetingsResearchPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionMeetingsResearchMapper;
import tech.piis.modules.core.service.IInspectionMeetingsResearchService;
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
 * 会议研究Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@Transactional
@Slf4j
@Service
public class InspectionMeetingsResearchServiceImpl implements IInspectionMeetingsResearchService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionMeetingsResearchMapper inspectionMeetingsResearchMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String BIZ_TIME = "MeetingsResearch";

    /**
     * 统计巡视方案下被巡视单位InspectionMeetingsResearch次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionMeetingsResearchCount(String planId) throws BaseException {
        return inspectionMeetingsResearchMapper.selectInspectionMeetingsResearchCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询会议研究列表
     *
     * @param inspectionMeetingsResearch
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionMeetingsResearchPO> selectInspectionMeetingsResearchList(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException {
        QueryWrapper<InspectionMeetingsResearchPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionMeetingsResearch.getUnitsId());
        queryWrapper.eq("plan_id", inspectionMeetingsResearch.getPlanId());
        List<InspectionMeetingsResearchPO> inspectionMeetingsResearchList = inspectionMeetingsResearchMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionMeetingsResearchList)) {
            inspectionMeetingsResearchList.forEach(var -> var.setDocuments(documentService.getFileListByBizId(BIZ_TIME + var.getMeetingsResearchId())));
        }
        return inspectionMeetingsResearchList;
    }

    /**
     * 新增会议研究
     *
     * @param inspectionMeetingsResearch
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException {
        int result = inspectionMeetingsResearchMapper.insert(inspectionMeetingsResearch);
        List<PiisDocumentPO> documents = inspectionMeetingsResearch.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionMeetingsResearch.getMeetingsResearchId();
        documentService.updateDocumentBatch(documents, BIZ_TIME + bizId, FileEnum.MEETINGS_RESEARCH_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改会议研究
     *
     * @param inspectionMeetingsResearch
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException {
        Object bizId = inspectionMeetingsResearch.getMeetingsResearchId();
        documentService.updateDocumentBatch(inspectionMeetingsResearch.getDocuments(), BIZ_TIME + bizId, FileEnum.MEETINGS_RESEARCH_FILE.getCode());
        return inspectionMeetingsResearchMapper.updateById(inspectionMeetingsResearch);
    }

    /**
     * 根据ID批量删除会议研究
     *
     * @param meetingsResearchIds 会议研究编号
     * @return
     */
    @Override
    public int deleteByInspectionMeetingsResearchIds(Long[] meetingsResearchIds) throws BaseException {
        List<Long> list = Arrays.asList(meetingsResearchIds);
        return inspectionMeetingsResearchMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionMeetingsResearch
     */
    private void handleTodo(InspectionMeetingsResearchPO inspectionMeetingsResearch) {
        String planId = inspectionMeetingsResearch.getPlanId();
        Long unitsId = inspectionMeetingsResearch.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[会议研究]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionMeetingsResearch.getMeetingsResearchId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionMeetingsResearch.getApproverId())
                .setApproverName(inspectionMeetingsResearch.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionMeetingsResearchPO> inspectionMeetingsResearchList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionMeetingsResearchList)) {
            inspectionMeetingsResearchList.forEach(inspectionMeetingsResearch -> {
                inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionMeetingsResearchMapper.updateById(inspectionMeetingsResearch.setDocuments(null));
                handleTodo(inspectionMeetingsResearch);
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
        log.info("###ApplicationListener notify event [会议研究]###");
        Object object = event.getSource();
        if (object instanceof InspectionMeetingsResearchServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionMeetingsResearchPO inspectionMeetingsResearch = new InspectionMeetingsResearchPO()
                    .setMeetingsResearchId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                event.setData(inspectionMeetingsResearchMapper.selectInspectionMeetingsResearchWithFileById(inspectionMeetingsResearch));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionMeetingsResearchPO.class, inspectionMeetingsResearch);
                inspectionMeetingsResearchMapper.updateById(inspectionMeetingsResearch);
            }
        }
    }
}
