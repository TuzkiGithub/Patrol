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
import tech.piis.modules.core.domain.po.InspectionRectificationPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.RectificationCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionRectificationMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IInspectionRectificationService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.*;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 整改公开情况Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@Slf4j
@Transactional
@Service
public class InspectionRectificationServiceImpl implements IInspectionRectificationService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionRectificationMapper inspectionRectificationMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String BIZ_TIME = "Rectification";

    private static final Map<Long, Long> TO_DICT_MAP = new HashMap<>();
    private static final Map<Long, Long> TO_TEMP_MAP = new HashMap<>();

    static {
        TO_DICT_MAP.put(1L, FileEnum.RECTIFICATION_FILE_ONE.getCode());
        TO_DICT_MAP.put(2L, FileEnum.RECTIFICATION_FILE_TWO.getCode());
    }

    static {
        TO_TEMP_MAP.put(FileEnum.RECTIFICATION_FILE_ONE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.RECTIFICATION_FILE_TWO.getCode(), 2L);
    }

    /**
     * 统计巡视方案下被巡视单位InspectionRectification次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<RectificationCountVO> selectInspectionRectificationCount(String planId) throws BaseException {
        List<RectificationCountVO> rectificationCountList = inspectionRectificationMapper.selectInspectionRectificationCount(planId);
        if (!CollectionUtils.isEmpty(rectificationCountList)) {
            rectificationCountList = rectificationCountList.stream().sorted(Comparator.comparing(RectificationCountVO::getUnitsId).reversed()).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(rectificationCountList)) {
                rectificationCountList.forEach(rectificationCountVO -> {
                    List<PiisDocumentPO> documents = rectificationCountVO.getDocuments();
                    List<PiisDocumentPO> rectificationNoticeDoc = new ArrayList<>();
                    List<PiisDocumentPO> rectificationConditionDoc = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(documents)) {
                        documents.forEach(documentPO -> {
                            if (FileEnum.RECTIFICATION_FILE_ONE.getCode().equals(documentPO.getFileDictId())) {
                                rectificationNoticeDoc.add(documentPO);
                            }
                            if (FileEnum.RECTIFICATION_FILE_TWO.getCode().equals(documentPO.getFileDictId())) {
                                rectificationConditionDoc.add(documentPO);
                            }
                        });
                    }
                    rectificationCountVO.setRectificationNoticeDoc(rectificationNoticeDoc);
                    rectificationCountVO.setRectificationConditionDoc(rectificationConditionDoc);
                });
            }
        }
        return rectificationCountList;
    }

    /**
     * 查询整改公开情况列表
     *
     * @param inspectionRectification
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionRectificationPO> selectInspectionRectificationList(InspectionRectificationPO inspectionRectification) throws BaseException {
        QueryWrapper<InspectionRectificationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionRectification.getUnitsId());
        queryWrapper.eq("plan_id", inspectionRectification.getPlanId());
        List<InspectionRectificationPO> inspectionRectificationList = inspectionRectificationMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionRectificationList)) {
            inspectionRectificationList.forEach(var -> {
                List<PiisDocumentPO> documents = documentService.getFileListByBizId(BIZ_TIME + var.getRectificationId());
                BizUtils.convertFileDict(documents, TO_TEMP_MAP);
                var.setDocuments(documents);
            });
        }
        return inspectionRectificationList;
    }

    /**
     * 新增整改公开情况
     *
     * @param inspectionRectification
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionRectificationPO inspectionRectification) throws BaseException {
        BizUtils.convertFileDict(inspectionRectification.getDocuments(), TO_DICT_MAP);
        int result = inspectionRectificationMapper.insert(inspectionRectification);
        List<PiisDocumentPO> documents = inspectionRectification.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionRectification.getRectificationId();
        documentService.updateDocumentBatch(documents, BIZ_TIME + bizId);
        return result;
    }

    /**
     * 根据ID修改整改公开情况
     *
     * @param inspectionRectification
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionRectificationPO inspectionRectification) throws BaseException {
        BizUtils.convertFileDict(inspectionRectification.getDocuments(), TO_DICT_MAP);
        Object bizId = inspectionRectification.getRectificationId();
        documentService.updateDocumentBatch(inspectionRectification.getDocuments(), BIZ_TIME + bizId);
        return inspectionRectificationMapper.updateById(inspectionRectification);
    }

    /**
     * 根据ID批量删除整改公开情况
     *
     * @param rectificationIds 整改公开情况编号
     * @return
     */
    @Override
    public int deleteByInspectionRectificationIds(Long[] rectificationIds) throws BaseException {
        List<Long> list = Arrays.asList(rectificationIds);
        return inspectionRectificationMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionRectification
     */
    private void handleTodo(InspectionRectificationPO inspectionRectification) {
        String planId = inspectionRectification.getPlanId();
        Long unitsId = inspectionRectification.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[整改公开情况]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionRectification.getRectificationId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionRectification.getApproverId())
                .setApproverName(inspectionRectification.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionRectificationPO> inspectionRectificationList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionRectificationList)) {
            inspectionRectificationList.forEach(inspectionRectification -> {
                inspectionRectification.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionRectificationMapper.updateById(inspectionRectification.setDocuments(null));
                handleTodo(inspectionRectification);
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
        log.info("###ApplicationListener notify event [整改公开情况]###");
        Object object = event.getSource();
        if (object instanceof InspectionRectificationServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionRectificationPO inspectionRectification = new InspectionRectificationPO()
                    .setRectificationId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionRectificationPO result = inspectionRectificationMapper.selectInspectionRectificationWithFileById(inspectionRectification);
                List<PiisDocumentPO> documents = result.getDocuments();
                BizUtils.convertFileDict(documents, TO_TEMP_MAP);
                result.setDocuments(documents);
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionRectification.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionRectification.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionRectification.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionRectificationPO.class, inspectionRectification);
                inspectionRectificationMapper.updateById(inspectionRectification);
            }
        }
    }
}
