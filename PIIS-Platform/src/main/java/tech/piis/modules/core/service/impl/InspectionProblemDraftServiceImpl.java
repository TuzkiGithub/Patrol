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
import tech.piis.modules.core.domain.po.InspectionProblemDraftPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionProblemDraftMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IInspectionProblemDraftService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.*;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 问题底稿 Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-27
 */
@Transactional
@Slf4j
@Service
public class InspectionProblemDraftServiceImpl implements IInspectionProblemDraftService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionProblemDraftMapper inspectionProblemDraftMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    /**
     * 与前端接口定义文件类型
     */
    private static final Map<Long, Long> TO_DICT_MAP = new HashMap<>();
    private static final Map<Long, Long> TO_TEMP_MAP = new HashMap<>();

    static {
        TO_DICT_MAP.put(1L, FileEnum.PIIS_INFORMATION_FILE.getCode());
        TO_DICT_MAP.put(2L, FileEnum.SUPPORTING_MATERIALS_FILE.getCode());
        TO_DICT_MAP.put(3L, FileEnum.PROBLEM_DRAFT_FILE.getCode());
    }

    static {
        TO_TEMP_MAP.put(FileEnum.PIIS_INFORMATION_FILE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.SUPPORTING_MATERIALS_FILE.getCode(), 2L);
        TO_TEMP_MAP.put(FileEnum.PROBLEM_DRAFT_FILE.getCode(), 3L);
    }


    /**
     * 统计巡视方案下被巡视单位InspectionProblemDraft次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionProblemDraftCount(String planId) throws BaseException {
        return inspectionProblemDraftMapper.selectInspectionProblemDraftCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询问题底稿 列表
     *
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionProblemDraftPO> selectInspectionProblemDraftList(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        QueryWrapper<InspectionProblemDraftPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionProblemDraft.getUnitsId());
        queryWrapper.eq("plan_id", inspectionProblemDraft.getPlanId());
        queryWrapper.orderByDesc("created_time");
        return inspectionProblemDraftMapper.selectList(queryWrapper);
    }

    /**
     * 新增问题底稿
     *
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        int result = inspectionProblemDraftMapper.insert(inspectionProblemDraft);
        List<PiisDocumentPO> documents = inspectionProblemDraft.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionProblemDraft.getProblemDraftId();
        documentService.updateDocumentBatch(documents, "ProblemDraft" + bizId);
        return result;
    }

    /**
     * 根据ID修改问题底稿
     *
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        Object bizId = inspectionProblemDraft.getProblemDraftId();
        documentService.updateDocumentBatch(inspectionProblemDraft.getDocuments(), "ProblemDraft" + bizId);
        return inspectionProblemDraftMapper.updateById(inspectionProblemDraft);
    }

    /**
     * 根据ID批量删除问题底稿
     *
     * @param problemDraftIds 问题底稿 编号
     * @return
     */
    @Override
    public int deleteByInspectionProblemDraftIds(Long[] problemDraftIds) {
        List<Long> list = Arrays.asList(problemDraftIds);
        return inspectionProblemDraftMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionProblemDraftMapper.selectCount(null);
    }

    /**
     * 新增代办
     *
     * @param problemDraftPO
     */
    private void handleTodo(InspectionProblemDraftPO problemDraftPO) {
        String planId = problemDraftPO.getPlanId();
        Long unitsId = problemDraftPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[问题底稿]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(problemDraftPO.getProblemDraftId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(problemDraftPO.getApproverId())
                .setApproverName(problemDraftPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批问题底稿
     *
     * @param inspectionProblemDraftPOList
     */
    @Override
    public void doApprovals(List<InspectionProblemDraftPO> inspectionProblemDraftPOList) {
        if (!CollectionUtils.isEmpty(inspectionProblemDraftPOList)) {
            inspectionProblemDraftPOList.forEach(inspectionProblemDraftPO -> {
                inspectionProblemDraftPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionProblemDraftMapper.updateById(inspectionProblemDraftPO);
                handleTodo(inspectionProblemDraftPO);
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
        log.info("###ApplicationListener notify event [问题底稿]###");
        Object object = event.getSource();
        if (object instanceof InspectionProblemDraftServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionProblemDraftPO problemDraftPO = new InspectionProblemDraftPO()
                    .setProblemDraftId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionProblemDraftPO result = inspectionProblemDraftMapper.selectInspectionProblemDraftWithFile(problemDraftPO);
                BizUtils.convertFileDict(problemDraftPO.getDocuments(), TO_TEMP_MAP);
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        problemDraftPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        problemDraftPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    problemDraftPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionProblemDraftPO.class, problemDraftPO);
                inspectionProblemDraftMapper.updateById(problemDraftPO);
            }
        }
    }
}