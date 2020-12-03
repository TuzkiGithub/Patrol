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
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPO;
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPersonPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionInvestigationVisitMapper;
import tech.piis.modules.core.mapper.InspectionInvestigationVisitPersonMapper;
import tech.piis.modules.core.service.IInspectionInvestigationVisitService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 调研走访Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
@Slf4j
public class InspectionInvestigationVisitServiceImpl implements IInspectionInvestigationVisitService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionInvestigationVisitMapper inspectionInvestigationVisitMapper;

    @Autowired
    private InspectionInvestigationVisitPersonMapper inspectionInvestigationVisitPersonMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;


    /**
     * 统计巡视方案下被巡视单位InspectionInvestigationVisit次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionInvestigationVisitCount(String planId) throws BaseException {
        return inspectionInvestigationVisitMapper.selectInspectionInvestigationVisitCount(planId);
    }

    /**
     * 查询调研走访列表
     *
     * @param inspectionInvestigationVisit
     * @return
     * @throws BaseException
     */
    public List<InspectionInvestigationVisitPO> selectInspectionInvestigationVisitList(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException {
        return inspectionInvestigationVisitMapper.selectInvestigationVisitList(inspectionInvestigationVisit);
    }

    /**
     * 新增调研走访
     *
     * @param inspectionInvestigationVisit
     * @return
     * @throws BaseException
     */
    public int save(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException {
        String investigationVisitId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        //设置主键
        inspectionInvestigationVisit.setInvestigationVisitId(investigationVisitId);
        List<PiisDocumentPO> documents = inspectionInvestigationVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> document.setOperationType(INSERT));
        }
        documentService.updateDocumentBatch(documents, "InvestigationVisit" + investigationVisitId, FileEnum.INVESTIGATION_OTHER_FILE.getCode());

        //新增调研走访人员
        List<InspectionInvestigationVisitPersonPO> inspectionInvestigationVisitPersonList = inspectionInvestigationVisit.getInvestigationVisitPersonList();
        if (!CollectionUtils.isEmpty(inspectionInvestigationVisitPersonList)) {
            inspectionInvestigationVisitPersonList.forEach(var -> {
                var.setInvestigationVisitId(investigationVisitId);
                inspectionInvestigationVisitPersonMapper.insert(var);
            });
        }

        return inspectionInvestigationVisitMapper.insert(inspectionInvestigationVisit.setInvestigationVisitPersonList(null));
    }

    /**
     * 根据ID修改调研走访
     *
     * @param inspectionInvestigationVisit
     * @return
     * @throws BaseException
     */
    public int update(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException {
        documentService.updateDocumentBatch(inspectionInvestigationVisit.getDocuments(), "InvestigationVisit" + inspectionInvestigationVisit.getInvestigationVisitId(), FileEnum.INVESTIGATION_OTHER_FILE.getCode());

        //更新调研走访人员
        List<InspectionInvestigationVisitPersonPO> investigationVisitPersonList = inspectionInvestigationVisit.getInvestigationVisitPersonList();
        if (!CollectionUtils.isEmpty(investigationVisitPersonList)) {
            for (InspectionInvestigationVisitPersonPO var : investigationVisitPersonList) {
                var.setInvestigationVisitId(inspectionInvestigationVisit.getInvestigationVisitId());
                Integer operationType = var.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            inspectionInvestigationVisitPersonMapper.insert(var);
                            break;
                        case UPDATE:
                            inspectionInvestigationVisitPersonMapper.updateById(var);
                            break;
                        case DELETE:
                            inspectionInvestigationVisitPersonMapper.deleteById(var.getInvestigationVisitPersonId());
                            break;
                    }
                }
            }

        }


        return inspectionInvestigationVisitMapper.updateById(inspectionInvestigationVisit.setInvestigationVisitPersonList(null));
    }

    /**
     * 根据ID批量删除调研走访
     *
     * @param investigationVisitIds 调研走访编号
     * @return
     */
    public int deleteByInspectionInvestigationVisitIds(String[] investigationVisitIds) {
        List<String> list = Arrays.asList(investigationVisitIds);
        return inspectionInvestigationVisitMapper.deleteBatchIds(list);
    }

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    public int count(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException {
        QueryWrapper<InspectionInvestigationVisitPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UNITS_ID", inspectionInvestigationVisit.getUnitsId());
        queryWrapper.eq("PLAN_ID", inspectionInvestigationVisit.getPlanId());
        return inspectionInvestigationVisitMapper.selectCount(queryWrapper);
    }

    /**
     * 新增代办
     *
     * @param investigationVisitPO
     */
    private void handleTodo(InspectionInvestigationVisitPO investigationVisitPO) {
        String planId = investigationVisitPO.getPlanId();
        Long unitsId = investigationVisitPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[走访调研]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(investigationVisitPO.getInvestigationVisitId())
                .setTodoStatus(TODO_NEED)
                .setApproverId(investigationVisitPO.getApproverId())
                .setApproverName(investigationVisitPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批
     *
     * @param investigationVisitList
     */
    @Override
    public void doApprovals(List<InspectionInvestigationVisitPO> investigationVisitList) {
        if (!CollectionUtils.isEmpty(investigationVisitList)) {
            investigationVisitList.forEach(investigationVisit -> {
                investigationVisit.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionInvestigationVisitMapper.updateById(investigationVisit);
                handleTodo(investigationVisit);
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
        System.out.println("###ApplicationListener notify event [调研走访]###");
        log.info("###ApplicationListener notify event [调研走访]###");
        Object object = event.getSource();
        if (object instanceof InspectionInvestigationVisitServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionInvestigationVisitPO investigationVisitPO = new InspectionInvestigationVisitPO()
                    .setInvestigationVisitId(event.getBizId());
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionInvestigationVisitPO result = inspectionInvestigationVisitMapper.selectInvestigationVisitWithFile(investigationVisitPO);
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        investigationVisitPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        investigationVisitPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    investigationVisitPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionInvestigationVisitPO.class, investigationVisitPO);
                inspectionInvestigationVisitMapper.updateById(investigationVisitPO);
            }
        }
    }
}
