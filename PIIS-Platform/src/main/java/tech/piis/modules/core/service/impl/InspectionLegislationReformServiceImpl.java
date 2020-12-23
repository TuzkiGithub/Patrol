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
import tech.piis.modules.core.domain.po.InspectionLegislationReformPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionLegislationReformMapper;
import tech.piis.modules.core.service.IInspectionLegislationReformService;
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
 * 立行立改 Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-23
 */
@Transactional
@Service
@Slf4j
public class InspectionLegislationReformServiceImpl implements IInspectionLegislationReformService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionLegislationReformMapper inspectionLegislationReformMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    /**
     * 统计巡视方案下被巡视单位InspectionLegislationReform次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionLegislationReformCount(String planId) throws BaseException {
        return inspectionLegislationReformMapper.selectInspectionLegislationReformCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询立行立改 列表
     *
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionLegislationReformPO> selectInspectionLegislationReformList(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException {
        QueryWrapper<InspectionLegislationReformPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionLegislationReform.getUnitsId());
        queryWrapper.eq("plan_id", inspectionLegislationReform.getPlanId());
        return inspectionLegislationReformMapper.selectList(queryWrapper);
    }

    /**
     * 新增立行立改
     *
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException {
        int result = inspectionLegislationReformMapper.insert(inspectionLegislationReform);
        List<PiisDocumentPO> documents = inspectionLegislationReform.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionLegislationReform.getLegislationReformId();
        documentService.updateDocumentBatch(documents, "LegislationReform" + bizId, FileEnum.LEGISLATION_REFORM_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改立行立改
     *
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException {
        Object bizId = inspectionLegislationReform.getLegislationReformId();
        documentService.updateDocumentBatch(inspectionLegislationReform.getDocuments(), "LegislationReform" + bizId, FileEnum.LEGISLATION_REFORM_FILE.getCode());
        return inspectionLegislationReformMapper.updateById(inspectionLegislationReform);
    }

    /**
     * 根据ID批量删除立行立改
     *
     * @param legislationReformIds 立行立改 编号
     * @return
     */
    @Override
    public int deleteByInspectionLegislationReformIds(Long[] legislationReformIds) {
        List<Long> list = Arrays.asList(legislationReformIds);
        return inspectionLegislationReformMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionLegislationReformMapper.selectCount(null);
    }

    /**
     * 新增代办
     *
     * @param inspectionLegislationReformPO
     */
    private void handleTodo(InspectionLegislationReformPO inspectionLegislationReformPO) {
        String planId = inspectionLegislationReformPO.getPlanId();
        Long unitsId = inspectionLegislationReformPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[立行立改]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionLegislationReformPO.getLegislationReformId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionLegislationReformPO.getApproverId())
                .setApproverName(inspectionLegislationReformPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批立行立改
     *
     * @param inspectionLegislationReformList
     */
    @Override
    public void approvals(List<InspectionLegislationReformPO> inspectionLegislationReformList) {
        if (!CollectionUtils.isEmpty(inspectionLegislationReformList)) {
            inspectionLegislationReformList.forEach(inspectionLegislationReformPO -> {
                inspectionLegislationReformPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionLegislationReformMapper.updateById(inspectionLegislationReformPO);
                handleTodo(inspectionLegislationReformPO);
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
        log.info("###ApplicationListener notify event [立行立改]###");
        Object object = event.getSource();
        if (object instanceof InspectionLegislationReformServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionLegislationReformPO inspectionLegislationReformPO = new InspectionLegislationReformPO()
                    .setLegislationReformId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                event.setData(inspectionLegislationReformMapper.selectLegislationReform(inspectionLegislationReformPO));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionLegislationReformPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionLegislationReformPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionLegislationReformPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionLegislationReformPO.class, inspectionLegislationReformPO);
                inspectionLegislationReformMapper.updateById(inspectionLegislationReformPO);
            }
        }
    }
}
