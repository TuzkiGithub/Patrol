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
import tech.piis.modules.core.domain.po.InspectionForumPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionForumMapper;
import tech.piis.modules.core.service.IInspectionForumService;
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
 * 座谈会 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-07
 */
@Transactional
@Service
@Slf4j
public class InspectionForumServiceImpl implements IInspectionForumService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionForumMapper inspectionForumMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String bizName = "Forum";

    /**
     * 统计巡视方案下被巡视单位InspectionForum次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionForumCount(String planId) throws BaseException {
        return inspectionForumMapper.selectInspectionForumCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询座谈会 列表
     *
     * @param inspectionForum
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionForumPO> selectInspectionForumList(InspectionForumPO inspectionForum) throws BaseException {
        QueryWrapper<InspectionForumPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionForum.getUnitsId());
        queryWrapper.eq("plan_id", inspectionForum.getPlanId());
        queryWrapper.orderByDesc("created_time");
        List<InspectionForumPO> inspectionForumList = inspectionForumMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionForumList)) {
            inspectionForumList.forEach(var -> var.setDocuments(documentService.getFileListByBizId(bizName + var.getForumId())));
        }
        return inspectionForumList;
    }

    /**
     * 新增座谈会
     *
     * @param inspectionForum
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionForumPO inspectionForum) throws BaseException {
        int result = inspectionForumMapper.insert(inspectionForum);
        List<PiisDocumentPO> documents = inspectionForum.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionForum.getForumId();
        documentService.updateDocumentBatch(documents, bizName + bizId, FileEnum.FORUM_OTHER_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改座谈会
     *
     * @param inspectionForum
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionForumPO inspectionForum) throws BaseException {
        Object bizId = inspectionForum.getForumId();
        documentService.updateDocumentBatch(inspectionForum.getDocuments(), bizName + bizId, FileEnum.FORUM_OTHER_FILE.getCode());
        return inspectionForumMapper.updateById(inspectionForum);
    }

    /**
     * 根据ID批量删除座谈会
     *
     * @param forumIds 座谈会 编号
     * @return
     */
    @Override
    public int deleteByInspectionForumIds(Long[] forumIds) throws BaseException {
        List<Long> list = Arrays.asList(forumIds);
        return inspectionForumMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionForum
     */
    private void handleTodo(InspectionForumPO inspectionForum) {
        String planId = inspectionForum.getPlanId();
        Long unitsId = inspectionForum.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[座谈会]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionForum.getForumId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionForum.getApproverId())
                .setApproverName(inspectionForum.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionForumPO> inspectionForumList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionForumList)) {
            inspectionForumList.forEach(inspectionForum -> {
                inspectionForum.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionForumMapper.updateById(inspectionForum.setDocuments(null));
                handleTodo(inspectionForum);
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
        log.info("###ApplicationListener notify event [座谈会]###");
        Object object = event.getSource();
        if (object instanceof InspectionForumServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionForumPO inspectionForum = new InspectionForumPO()
                    .setForumId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionForumPO result = inspectionForumMapper.selectInspectionForumWithFileById(inspectionForum);
                result.setReporter(BizUtils.paramsCovert2List(result.getReporterId(), result.getReporterName()));
                result.setParticipants(BizUtils.paramsCovert2List(result.getReportPersonId(), result.getReportPersonName()));
                result.setInspectionGroupPersons(BizUtils.paramsCovert2List(result.getInspectionGroupPersonId(), result.getInspectionGroupPersonName()));
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionForum.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionForum.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionForum.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionForumPO.class, inspectionForum);
                inspectionForumMapper.updateById(inspectionForum);
            }
        }
    }
}
