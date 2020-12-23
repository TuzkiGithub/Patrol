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
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingDetailPO;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionSinkingUnderstandingDetailMapper;
import tech.piis.modules.core.mapper.InspectionSinkingUnderstandingMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IInspectionSinkingUnderstandingService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.*;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 下沉了解Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
@Slf4j
public class InspectionSinkingUnderstandingServiceImpl implements IInspectionSinkingUnderstandingService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionSinkingUnderstandingMapper inspectionSinkingUnderstandingMapper;

    @Autowired
    private InspectionSinkingUnderstandingDetailMapper inspectionSinkingUnderstandingDetailMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstanding次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionSinkingUnderstandingCount(String planId) throws BaseException {
        return inspectionSinkingUnderstandingMapper.selectInspectionSinkingUnderstandingCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询下沉了解列表
     *
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public List<InspectionSinkingUnderstandingPO> selectInspectionSinkingUnderstandingList(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        return inspectionSinkingUnderstandingMapper.selectInspectionSinkingUnderstandingList(inspectionSinkingUnderstanding);
    }

    /**
     * 新增下沉了解
     *
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public int save(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        //设置主键
        String id = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionSinkingUnderstanding.setSinkingUnderstandingId(id);

        List<PiisDocumentPO> documents = inspectionSinkingUnderstanding.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        documentService.updateDocumentBatch(documents, "SinkingUnderstanding" + id, FileEnum.SINKING_OTHER_FILE.getCode());

        //新增下沉了解详情
        List<InspectionSinkingUnderstandingDetailPO> sinkingUnderstandingDetails = inspectionSinkingUnderstanding.getSinkingUnderstandingDetailList();
        if (!CollectionUtils.isEmpty(sinkingUnderstandingDetails)) {
            sinkingUnderstandingDetails.forEach(var -> inspectionSinkingUnderstandingDetailMapper.insert(var.setSinkingUnderstandingId(id)));
        }
        return inspectionSinkingUnderstandingMapper.insert(inspectionSinkingUnderstanding.setSinkingUnderstandingDetailList(null));
    }

    /**
     * 根据ID修改下沉了解
     *
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public int update(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        //更新下沉了解文件
        documentService.updateDocumentBatch(inspectionSinkingUnderstanding.getDocuments(), "SinkingUnderstanding" + inspectionSinkingUnderstanding.getSinkingUnderstandingId(), FileEnum.SINKING_OTHER_FILE.getCode());

        //更新下沉了解详情
        List<InspectionSinkingUnderstandingDetailPO> sinkingUnderstandingDetailList = inspectionSinkingUnderstanding.getSinkingUnderstandingDetailList();
        if (!CollectionUtils.isEmpty(sinkingUnderstandingDetailList)) {
            sinkingUnderstandingDetailList.forEach(var -> var.setSinkingUnderstandingId(inspectionSinkingUnderstanding.getSinkingUnderstandingId()));
            for (InspectionSinkingUnderstandingDetailPO sinkingUnderstandingDetail : sinkingUnderstandingDetailList) {
                Integer operationType = sinkingUnderstandingDetail.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            inspectionSinkingUnderstandingDetailMapper.insert(sinkingUnderstandingDetail);
                            break;
                        case UPDATE:
                            inspectionSinkingUnderstandingDetailMapper.updateById(sinkingUnderstandingDetail);
                            break;
                        case DELETE:
                            inspectionSinkingUnderstandingDetailMapper.deleteById(sinkingUnderstandingDetail.getSinkingUnderstandingDetailId());
                            break;
                    }
                }
            }
        }
        return inspectionSinkingUnderstandingMapper.updateById(inspectionSinkingUnderstanding.setSinkingUnderstandingDetailList(null));
    }

    /**
     * 根据ID批量删除下沉了解
     *
     * @param sinkingUnderstandingIds 下沉了解编号
     * @return
     */
    public int deleteByInspectionSinkingUnderstandingIds(String[] sinkingUnderstandingIds) {
        List<String> list = Arrays.asList(sinkingUnderstandingIds);
        return inspectionSinkingUnderstandingMapper.deleteBatchIds(list);
    }

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    public int count(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) {
        QueryWrapper<InspectionSinkingUnderstandingPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UNITS_ID", inspectionSinkingUnderstanding.getUnitsId());
        queryWrapper.eq("PLAN_ID", inspectionSinkingUnderstanding.getPlanId());
        return inspectionSinkingUnderstandingMapper.selectCount(queryWrapper);
    }

    /**
     * 新增代办
     *
     * @param sinkingUnderstandingPO
     */
    private void handleTodo(InspectionSinkingUnderstandingPO sinkingUnderstandingPO) {
        String planId = sinkingUnderstandingPO.getPlanId();
        Long unitsId = sinkingUnderstandingPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[下沉了解]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(sinkingUnderstandingPO.getSinkingUnderstandingId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(sinkingUnderstandingPO.getApproverId())
                .setApproverName(sinkingUnderstandingPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @param sinkingUnderstandingList
     */
    @Override
    public void doApprovals(List<InspectionSinkingUnderstandingPO> sinkingUnderstandingList) {
        if (!CollectionUtils.isEmpty(sinkingUnderstandingList)) {
            sinkingUnderstandingList.forEach(sinkingUnderstanding -> {
                sinkingUnderstanding.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionSinkingUnderstandingMapper.updateById(sinkingUnderstanding);
                handleTodo(sinkingUnderstanding);
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
        log.info("###ApplicationListener notify event [下沉了解]###");
        Object object = event.getSource();
        if (object instanceof InspectionSinkingUnderstandingServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionSinkingUnderstandingPO sinkingUnderstandingPO = new InspectionSinkingUnderstandingPO()
                    .setSinkingUnderstandingId(event.getBizId());
            if (OperationEnum.SELECT.getCode() == eventType) {
                event.setData(inspectionSinkingUnderstandingMapper.selectSinkingUnderstandingWithFile(sinkingUnderstandingPO));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        sinkingUnderstandingPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        sinkingUnderstandingPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    sinkingUnderstandingPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionSinkingUnderstandingPO.class, sinkingUnderstandingPO);
                inspectionSinkingUnderstandingMapper.updateById(sinkingUnderstandingPO);
            }
        }
    }

}
