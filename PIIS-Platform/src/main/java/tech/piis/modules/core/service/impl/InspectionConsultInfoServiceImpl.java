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
import tech.piis.modules.core.domain.po.InspectionConsultInfoDetailPO;
import tech.piis.modules.core.domain.po.InspectionConsultInfoPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionConsultInfoDetailMapper;
import tech.piis.modules.core.mapper.InspectionConsultInfoMapper;
import tech.piis.modules.core.service.IInspectionConsultInfoService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 查阅资料Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
@Slf4j
public class InspectionConsultInfoServiceImpl implements IInspectionConsultInfoService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionConsultInfoMapper consultInfoMapper;

    @Autowired
    private InspectionConsultInfoDetailMapper consultInfoDetailMapper;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 统计巡视方案下被巡视单位InspectionConsultInfo次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionConsultInfoCount(String planId) throws BaseException {
        return consultInfoMapper.selectInspectionConsultInfoCount(planId);
    }

    /**
     * 查询查阅资料列表
     *
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    public List<InspectionConsultInfoPO> selectInspectionConsultInfoList(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        return consultInfoMapper.selectInspectionConsultInfoList(inspectionConsultInfo);
    }

    /**
     * 新增查阅资料
     *
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    public int save(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        String consultInfoId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        //设置主键
        inspectionConsultInfo.setConsultInfoId(consultInfoId);

        //更新查阅资料文件业务字段
        List<PiisDocumentPO> documents = inspectionConsultInfo.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> document.setOperationType(INSERT));
        }
        documentService.updateDocumentBatch(documents, "ConsultInfo" + consultInfoId, FileEnum.CONSULT_INFO_FILE.getCode());

        //新增查阅资料详情
        List<InspectionConsultInfoDetailPO> consultInfoDetailList = inspectionConsultInfo.getConsultInfoDetailList();
        if (!CollectionUtils.isEmpty(consultInfoDetailList)) {
            consultInfoDetailList.forEach(var -> {
                var.setConsultInfoId(consultInfoId);
                BizUtils.setCreatedOperation(InspectionConsultInfoDetailPO.class, var);
            });
            consultInfoDetailMapper.insertBatch(consultInfoDetailList);
            //更新查阅资料详情文件业务字段
            for (InspectionConsultInfoDetailPO consultInfoDetail : consultInfoDetailList) {
                List<PiisDocumentPO> documentList = consultInfoDetail.getDocuments();
                documentList.forEach(document -> document.setOperationType(INSERT));
                documentService.updateDocumentBatch(consultInfoDetail.getDocuments(), "ConsultInfoDetail" + consultInfoDetail.getConsultInfoDetailId(), FileEnum.CONSULT_DETAIL_FILE.getCode());
            }
        }
        BizUtils.setCreatedOperation(InspectionConsultInfoPO.class, inspectionConsultInfo);
        return consultInfoMapper.insert(inspectionConsultInfo.setConsultInfoDetailList(null).setDocuments(null));
    }

    /**
     * 根据ID修改查阅资料
     *
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    public int update(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        //更新查询资料文件信息
        documentService.updateDocumentBatch(inspectionConsultInfo.getDocuments(), "ConsultInfo" + inspectionConsultInfo.getConsultInfoId(), FileEnum.CONSULT_INFO_FILE.getCode());

        //更新查阅资料详情
        List<InspectionConsultInfoDetailPO> consultInfoDetailList = inspectionConsultInfo.getConsultInfoDetailList();

        if (!CollectionUtils.isEmpty(consultInfoDetailList)) {
            for (InspectionConsultInfoDetailPO consultInfoDetail : consultInfoDetailList) {
                consultInfoDetail.setConsultInfoId(inspectionConsultInfo.getConsultInfoId());
                Integer operationType = consultInfoDetail.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            saveConsultInfoDetail(consultInfoDetail);
                            break;
                        case UPDATE:
                            consultInfoDetailMapper.updateById(consultInfoDetail);
                            documentService.updateDocumentBatch(consultInfoDetail.getDocuments(), "ConsultInfoDetail" + consultInfoDetail.getConsultInfoDetailId(), FileEnum.CONSULT_DETAIL_FILE.getCode());
                            break;
                        case DELETE:
                            delConsultInfoDetail(consultInfoDetail);
                            break;
                    }
                }
            }
        }
        return consultInfoMapper.updateById(inspectionConsultInfo.setConsultInfoDetailList(null).setDocuments(null));
    }

    /**
     * 根据ID批量删除查阅资料
     *
     * @param consultInfoIds 查阅资料编号
     * @return
     */
    public int deleteByInspectionConsultInfoIds(String[] consultInfoIds) {
        List<String> list = Arrays.asList(consultInfoIds);
        for (String consultInfoId : consultInfoIds) {
            QueryWrapper<InspectionConsultInfoDetailPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CONSULT_INFO_ID", consultInfoId);
            consultInfoDetailMapper.delete(queryWrapper);
        }
        return consultInfoMapper.deleteBatchIds(list);
    }

    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(InspectionConsultInfoPO inspectionConsultInfo) {
        QueryWrapper<InspectionConsultInfoPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UNITS_ID", inspectionConsultInfo.getUnitsId());
        queryWrapper.eq("PLAN_ID", inspectionConsultInfo.getPlanId());
        return consultInfoMapper.selectCount(queryWrapper);
    }

    /**
     * 新增查阅资料详情
     *
     * @param consultInfoDetail
     */
    private void saveConsultInfoDetail(InspectionConsultInfoDetailPO consultInfoDetail) {
        consultInfoDetailMapper.insert(consultInfoDetail);
        documentService.updateDocumentBatch(consultInfoDetail.getDocuments(), "ConsultInfoDetail" + consultInfoDetail.getConsultInfoDetailId(), FileEnum.CONSULT_DETAIL_FILE.getCode());
    }


    /**
     * 删除查阅系列详情
     *
     * @param consultInfoDetail
     */
    private void delConsultInfoDetail(InspectionConsultInfoDetailPO consultInfoDetail) {
        consultInfoDetailMapper.deleteById(consultInfoDetail.getConsultInfoDetailId());
        List<PiisDocumentPO> documents = consultInfoDetail.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> documentService.deleteDocumentById(document.getPiisDocId(), document.getFilePath()));
        }
    }

    /**
     * 新增代办
     *
     * @param inspectionConsultInfo
     */
    private void handleTodo(InspectionConsultInfoPO inspectionConsultInfo) {
        String planId = inspectionConsultInfo.getPlanId();
        Long unitsId = inspectionConsultInfo.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[查阅资料]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(inspectionConsultInfo.getConsultInfoId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(inspectionConsultInfo.getApproverId())
                .setApproverName(inspectionConsultInfo.getApproverName());
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }


    /**
     * 审批
     *
     * @param inspectionConsultInfoList
     */
    @Override
    public void doApprovals(List<InspectionConsultInfoPO> inspectionConsultInfoList) {
        if (!CollectionUtils.isEmpty(inspectionConsultInfoList)) {
            inspectionConsultInfoList.forEach(consultInfo -> {
                consultInfo.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                consultInfoMapper.updateById(consultInfo.setConsultInfoDetailList(null).setDocuments(null));
                handleTodo(consultInfo);
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
        System.out.println("###ApplicationListener notify event [查阅资料]###");
        log.info("###ApplicationListener notify event [查阅资料]###");
        Object object = event.getSource();
        if (object instanceof InspectionConsultInfoServiceImpl) {
            Integer eventType = event.getEventType();
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionConsultInfoPO inspectionConsultInfo = consultInfoMapper.selectInspectionConsultInfoById(event.getBizId());
                List<InspectionConsultInfoDetailPO> consultInfoDetailList = inspectionConsultInfo.getConsultInfoDetailList();
                if (!CollectionUtils.isEmpty(consultInfoDetailList)) {
                    consultInfoDetailList.forEach(consultInfoDetail -> {
                        List<PiisDocumentPO> documents = documentService.getFileListByBizId("ConsultInfoDetail" + consultInfoDetail.getConsultInfoDetailId());
                        consultInfoDetail.setDocuments(documents);
                    });
                }
                event.setData(inspectionConsultInfo);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                InspectionConsultInfoPO inspectionConsultInfoPO = new InspectionConsultInfoPO()
                        .setConsultInfoId(event.getBizId());
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionConsultInfoPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionConsultInfoPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionConsultInfoPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionConsultInfoPO.class, inspectionConsultInfoPO);
                consultInfoMapper.updateById(inspectionConsultInfoPO);
            }
        }
    }
}
