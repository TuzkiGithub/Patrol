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
import tech.piis.modules.core.domain.po.InspectionClueTransferDetailPO;
import tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionClueTransferDetailMapper;
import tech.piis.modules.core.mapper.InspectionClueTransferMapper;
import tech.piis.modules.core.service.IInspectionClueTransferService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.*;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.*;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 线索移交 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Transactional
@Service
@Slf4j
public class InspectionClueTransferServiceImpl implements IInspectionClueTransferService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionClueTransferMapper inspectionClueTransferMapper;

    @Autowired
    private InspectionClueTransferDetailMapper clueTransferDetailMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    /**
     * 与前端接口定义文件类型
     */
    private static final Map<Long, Long> TO_TEMP_MAP = new HashMap<>();

    static {
        TO_TEMP_MAP.put(FileEnum.APPROVAL_FILE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.HANDOVER_FILE.getCode(), 2L);
    }


    /**
     * 统计巡视方案下被巡视单位InspectionClueTransfer次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionClueTransferCount(String planId) throws BaseException {
        return inspectionClueTransferMapper.selectInspectionClueTransferCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询线索移交 列表
     *
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionClueTransferPO> selectInspectionClueTransferList(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        return inspectionClueTransferMapper.selectInspectionClueTransferList(inspectionClueTransfer);
    }

    /**
     * 新增线索移交
     *
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        //设置主键
        String clueTransferId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionClueTransfer.setClueTransferId(clueTransferId);

        //新增线索详情
        List<InspectionClueTransferDetailPO> clueTransferDetailList = inspectionClueTransfer.getClueTransferDetailList();
        if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
            clueTransferDetailList.forEach(var -> {
                var.setClueTransferId(clueTransferId);
                List<PiisDocumentPO> documents = var.getDocuments();
                clueTransferDetailMapper.insert(var.setDocuments(null));
                documents.forEach(document -> {
                            document.setOperationType(INSERT)
                                    .setObjectId("ClueTransferDetail" + var.getClueTransferDetailId());
                            documentService.updateDocumentById(document);
                        }
                );
            });
        }
        return inspectionClueTransferMapper.insert(inspectionClueTransfer.setClueTransferDetailList(null));
    }

    /**
     * 根据ID修改线索移交
     *
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        //修改线索详情
        List<InspectionClueTransferDetailPO> clueTransferDetailList = inspectionClueTransfer.getClueTransferDetailList();
        if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
            clueTransferDetailList.forEach(var -> {
                //查询已存在的文件-线索详情
                QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("OBJECT_ID", "ClueTransferDetail" + var.getClueTransferDetailId());
                List<PiisDocumentPO> oldDocuments = documentService.selectDocumentByCondition(queryWrapper);
                List<PiisDocumentPO> documents = var.getDocuments();
                Integer operationType = var.getOperationType();
                var.setClueTransferId(inspectionClueTransfer.getClueTransferId());

                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            List<PiisDocumentPO> tempDoc = var.getDocuments();
                            clueTransferDetailMapper.insert(var.setDocuments(null));
                            tempDoc.forEach(document -> document.setOperationType(INSERT));
                            documentService.updateDocumentBatch(tempDoc, "ClueTransferDetail" + var.getClueTransferDetailId());
                            break;
                        case UPDATE:
                            //标识需要删除的文件
                            /**
                             * oldDoc   1,2,3    1,2,3   1,2
                             * newDoc   3,4      1,3     1,2,3
                             * remove   1,2      2       NULL
                             */
                            if (!CollectionUtils.isEmpty(oldDocuments) && !CollectionUtils.isEmpty(documents)) {
                                oldDocuments.forEach(oldDocument -> {
                                    int count = 0;
                                    for (PiisDocumentPO newDocument : documents) {
                                        if (!oldDocument.getPiisDocId().equals(newDocument.getPiisDocId())) {
                                            count++;
                                        }
                                    }
                                    if (count == documents.size()) {
                                        oldDocument.setOperationType(DELETE);
                                    }
                                });
                            }
                            //文件先更新后删除原有数据
                            if (!CollectionUtils.isEmpty(documents)) {
                                documents.forEach(temp -> temp.setOperationType(INSERT));
                            }
                            documentService.updateDocumentBatch(documents, "ClueTransferDetail" + var.getClueTransferDetailId());
                            documentService.updateDocumentBatch(oldDocuments, "ClueTransferDetail" + var.getClueTransferDetailId());
                            clueTransferDetailMapper.updateById(var.setDocuments(null));
                            break;
                        case DELETE:
                            clueTransferDetailMapper.deleteById(var.getClueTransferDetailId());
                            documents.forEach(document -> document.setOperationType(DELETE));
                            documentService.updateDocumentBatch(documents, "ClueTransferDetail" + var.getClueTransferDetailId());
                            break;
                    }
                }

            });
        }
        return inspectionClueTransferMapper.updateById(inspectionClueTransfer.setClueTransferDetailList(null));
    }

    /**
     * 根据ID批量删除线索移交
     *
     * @param clueTransferIds 线索移交 编号
     * @return
     */
    @Override
    public int deleteByInspectionClueTransferIds(String[] clueTransferIds) {
        List<String> list = Arrays.asList(clueTransferIds);
        list.forEach(clueTransferId -> {
            QueryWrapper<InspectionClueTransferDetailPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CLUE_TRANSFER_ID", clueTransferId);
            clueTransferDetailMapper.delete(queryWrapper);
        });
        return inspectionClueTransferMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(InspectionClueTransferPO inspectionClueTransfer) {
        QueryWrapper<InspectionClueTransferPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionClueTransfer.getPlanId());
        queryWrapper.eq("UNITS_ID", inspectionClueTransfer.getUnitsId());
        return inspectionClueTransferMapper.selectCount(queryWrapper);
    }

    /**
     * 新增代办
     *
     * @param clueTransferPO
     */
    private void handleTodo(InspectionClueTransferPO clueTransferPO) {
        String planId = clueTransferPO.getPlanId();
        Long unitsId = clueTransferPO.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[线索移交]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(clueTransferPO.getClueTransferId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(clueTransferPO.getApproverId())
                .setApproverName(clueTransferPO.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批线索移交
     *
     * @param clueTransferList
     */
    @Override
    public void doApprovals(List<InspectionClueTransferPO> clueTransferList) {
        if (!CollectionUtils.isEmpty(clueTransferList)) {
            clueTransferList.forEach(clueTransferPO -> {
                clueTransferPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionClueTransferMapper.updateById(clueTransferPO);
                handleTodo(clueTransferPO);
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
        log.info("###ApplicationListener notify event [线索移交]###");
        Object object = event.getSource();
        if (object instanceof InspectionClueTransferServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionClueTransferPO clueTransferPO = new InspectionClueTransferPO()
                    .setClueTransferId(event.getBizId());
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionClueTransferPO result = inspectionClueTransferMapper.selectInspectionClueTransferWithFile(clueTransferPO);
                List<InspectionClueTransferDetailPO> clueTransferDetailList = result.getClueTransferDetailList();
                if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
                    clueTransferDetailList.forEach(clueTransferDetail -> {
                        List<PiisDocumentPO> documents = clueTransferDetail.getDocuments();
                        BizUtils.convertFileDict(documents, TO_TEMP_MAP);
                    });
                }
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        clueTransferPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        clueTransferPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    clueTransferPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionClueTransferPO.class, clueTransferPO);
                inspectionClueTransferMapper.updateById(clueTransferPO);
            }
        }
    }
}
