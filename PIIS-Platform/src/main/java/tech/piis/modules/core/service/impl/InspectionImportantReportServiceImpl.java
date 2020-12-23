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
import tech.piis.modules.core.domain.po.InspectionImportantReportPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionImportantReportMapper;
import tech.piis.modules.core.service.IInspectionImportantReportService;
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
 * 重要情况专题报告 Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-23
 */
@Transactional
@Service
@Slf4j
public class InspectionImportantReportServiceImpl implements IInspectionImportantReportService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionImportantReportMapper inspectionImportantReportMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String BIZ_NAME = "ImportantReport";


    /**
     * 统计巡视方案下被巡视单位InspectionImportantReport次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionImportantReportCount(String planId) throws BaseException {
        return inspectionImportantReportMapper.selectInspectionImportantReportCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询重要情况专题报告 列表
     *
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionImportantReportPO> selectInspectionImportantReportList(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        QueryWrapper<InspectionImportantReportPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionImportantReport.getUnitsId());
        queryWrapper.eq("plan_id", inspectionImportantReport.getPlanId());
        queryWrapper.orderByDesc("created_time");
        List<InspectionImportantReportPO> importantReportList = inspectionImportantReportMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(importantReportList)) {
            importantReportList.forEach(importantReport -> importantReport.setForwardSendList(BizUtils.paramsCovert2List(importantReport.getReporterId(), importantReport.getReporterName())));
        }
        return importantReportList;
    }

    /**
     * 新增重要情况专题报告
     *
     * @param inspectionImportantReport
     * @return
     * @throws BaseException im
     */
    @Override
    public int save(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        inspectionImportantReport.setReporterId(BizUtils.paramsCovert2String(inspectionImportantReport.getForwardSendList()).get(0));
        inspectionImportantReport.setReporterName(BizUtils.paramsCovert2String(inspectionImportantReport.getForwardSendList()).get(1));
        int result = inspectionImportantReportMapper.insert(inspectionImportantReport);
        List<PiisDocumentPO> documents = inspectionImportantReport.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionImportantReport.getImportantReportId();
        documentService.updateDocumentBatch(documents, BIZ_NAME + bizId, FileEnum.IMPORTANT_SPORT_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改重要情况专题报告
     *
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        inspectionImportantReport.setReporterId(BizUtils.paramsCovert2String(inspectionImportantReport.getForwardSendList()).get(0));
        inspectionImportantReport.setReporterName(BizUtils.paramsCovert2String(inspectionImportantReport.getForwardSendList()).get(1));
        Object bizId = inspectionImportantReport.getImportantReportId();
        documentService.updateDocumentBatch(inspectionImportantReport.getDocuments(), BIZ_NAME + bizId, FileEnum.IMPORTANT_SPORT_FILE.getCode());
        return inspectionImportantReportMapper.updateById(inspectionImportantReport);
    }

    /**
     * 根据ID批量删除重要情况专题报告
     *
     * @param inspectionImportantReportIds 重要情况专题报告 编号
     * @return
     */
    @Override
    public int deleteByInspectionImportantReportIds(Long[] inspectionImportantReportIds) {
        List<Long> list = Arrays.asList(inspectionImportantReportIds);
        return inspectionImportantReportMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionImportantReportMapper.selectCount(null);
    }

    /**
     * 新增代办
     *
     * @param inspectionImportantReportPO
     */
    private void handleTodo(InspectionImportantReportPO inspectionImportantReportPO) {
        String planId = inspectionImportantReportPO.getPlanId();
        Long unitsId = inspectionImportantReportPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[重要情况报告]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionImportantReportPO.getImportantReportId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionImportantReportPO.getApproverId())
                .setApproverName(inspectionImportantReportPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批重要情况专题报告
     *
     * @param inspectionImportantReportList
     */
    @Override
    public void doApprovals(List<InspectionImportantReportPO> inspectionImportantReportList) {
        if (!CollectionUtils.isEmpty(inspectionImportantReportList)) {
            inspectionImportantReportList.forEach(importantReportPO -> {
                importantReportPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionImportantReportMapper.updateById(importantReportPO);
                handleTodo(importantReportPO);
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
        log.info("###ApplicationListener notify event [重要情况报告]###");
        Object object = event.getSource();
        if (object instanceof InspectionImportantReportServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionImportantReportPO importantReportPO = new InspectionImportantReportPO()
                    .setImportantReportId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionImportantReportPO result = inspectionImportantReportMapper.selectImportantReport(importantReportPO);
                result.setForwardSendList(BizUtils.paramsCovert2List(result.getReporterId(), result.getReporterName()));
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        importantReportPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        importantReportPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    importantReportPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionImportantReportPO.class, importantReportPO);
                inspectionImportantReportMapper.updateById(importantReportPO);
            }
        }
    }
}
