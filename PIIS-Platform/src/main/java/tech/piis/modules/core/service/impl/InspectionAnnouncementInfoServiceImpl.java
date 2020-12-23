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
import tech.piis.modules.core.domain.po.InspectionAnnouncementInfoPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.AnnouncementInfoCountVO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionAnnouncementInfoMapper;
import tech.piis.modules.core.service.IInspectionAnnouncementInfoService;
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
 * 公告信息 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-12-03
 */
@Service
@Slf4j
@Transactional
public class InspectionAnnouncementInfoServiceImpl implements IInspectionAnnouncementInfoService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionAnnouncementInfoMapper inspectionAnnouncementInfoMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    private static final String bizName = "AnnouncementInfo";

    /**
     * 统计巡视方案下被巡视单位InspectionAnnouncementInfo次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<AnnouncementInfoCountVO> selectInspectionAnnouncementInfoCount(String planId) throws BaseException {
        return inspectionAnnouncementInfoMapper.selectInspectionAnnouncementInfoCount(planId).stream().sorted(Comparator.comparing(AnnouncementInfoCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询公告信息 列表
     *
     * @param inspectionAnnouncementInfo
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionAnnouncementInfoPO> selectInspectionAnnouncementInfoList(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException {
        QueryWrapper<InspectionAnnouncementInfoPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionAnnouncementInfo.getUnitsId());
        queryWrapper.eq("plan_id", inspectionAnnouncementInfo.getPlanId());
        queryWrapper.orderByDesc("created_time");
        List<InspectionAnnouncementInfoPO> announcementInfoList = inspectionAnnouncementInfoMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(announcementInfoList)) {
            announcementInfoList.forEach(announcementInfo -> announcementInfo.setDocuments(documentService.getFileListByBizId(bizName + announcementInfo.getAnnouncementInfoId())));
        }
        return announcementInfoList;
    }

    /**
     * 新增公告信息
     *
     * @param inspectionAnnouncementInfo
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) {
        List<PiisDocumentPO> documents = inspectionAnnouncementInfo.getDocuments();
        int result = inspectionAnnouncementInfoMapper.insert(inspectionAnnouncementInfo);
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionAnnouncementInfo.getAnnouncementInfoId();
        documentService.updateDocumentBatch(documents, bizName + bizId, FileEnum.ANNOUNCEMENT_INFO_FILE.getCode());
        handleTodo(inspectionAnnouncementInfo);
        return result;
    }


    /**
     * 根据ID修改公告信息
     *
     * @param inspectionAnnouncementInfo
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException {
        Object bizId = inspectionAnnouncementInfo.getAnnouncementInfoId();
        documentService.updateDocumentBatch(inspectionAnnouncementInfo.getDocuments(), bizName + bizId, FileEnum.ANNOUNCEMENT_INFO_FILE.getCode());
        handleTodo(inspectionAnnouncementInfo);
        return inspectionAnnouncementInfoMapper.updateById(inspectionAnnouncementInfo);
    }

    /**
     * 根据ID批量删除公告信息
     *
     * @param announcementInfoIds 公告信息 编号
     * @return
     */
    @Override
    public int deleteByInspectionAnnouncementInfoIds(Long[] announcementInfoIds) throws BaseException {
        List<Long> list = Arrays.asList(announcementInfoIds);
        return inspectionAnnouncementInfoMapper.deleteBatchIds(list);
    }


    /**
     * 新增代办
     *
     * @param inspectionAnnouncementInfo
     */
    private void handleTodo(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) {
        if (inspectionAnnouncementInfo.isSubmitFlag()) {
            String planId = inspectionAnnouncementInfo.getPlanId();
            Long unitsId = inspectionAnnouncementInfo.getUnitsId();
            PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                    .setPlanId(planId)
                    .setUnitsId(unitsId);
            PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
            WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                    .setLookStatus(NO_LOOK)
                    .setTodoName("[公告信息]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                    .setBusinessId(String.valueOf(inspectionAnnouncementInfo.getAnnouncementInfoId()))
                    .setTodoStatus(TODO_NEED)
                    .setApproverId(inspectionAnnouncementInfo.getApproverId())
                    .setApproverName(inspectionAnnouncementInfo.getApproverName());
            BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
            todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
        }
    }

    /**
     * 批量审批
     *
     * @throws BaseException
     */
    @Override
    public void doApprovals(List<InspectionAnnouncementInfoPO> inspectionAnnouncementInfoList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionAnnouncementInfoList)) {
            inspectionAnnouncementInfoList.forEach(inspectionAnnouncementInfo -> {
                inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionAnnouncementInfoMapper.updateById(inspectionAnnouncementInfo.setDocuments(null));
                handleTodo(inspectionAnnouncementInfo);
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
        Object object = event.getSource();
        System.out.println(object.getClass());
        if (object instanceof InspectionAnnouncementInfoServiceImpl) {
            Integer eventType = event.getEventType();
            InspectionAnnouncementInfoPO inspectionAnnouncementInfo = new InspectionAnnouncementInfoPO()
                    .setAnnouncementInfoId(Long.valueOf(event.getBizId()));
            if (OperationEnum.SELECT.getCode() == eventType) {
                event.setData(inspectionAnnouncementInfoMapper.selectInspectionAnnouncementInfoWithFile(inspectionAnnouncementInfo));
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionAnnouncementInfoPO.class, inspectionAnnouncementInfo);
                inspectionAnnouncementInfoMapper.updateById(inspectionAnnouncementInfo);
            }
        }
    }
}
