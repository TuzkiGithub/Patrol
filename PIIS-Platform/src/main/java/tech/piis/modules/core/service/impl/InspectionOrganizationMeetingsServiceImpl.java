package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionOrganizationMeetingsPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionOrganizationMeetingsMapper;
import tech.piis.modules.core.service.IInspectionOrganizationMeetingsService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 组织会议Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Slf4j
@Transactional
@Service
public class InspectionOrganizationMeetingsServiceImpl implements IInspectionOrganizationMeetingsService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionOrganizationMeetingsMapper inspectionOrganizationMeetingsMapper;

    @Autowired
    private IInspectionPlanService planService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 文件类型
     */
    private static final Integer TEMP_ORG_OTHER_FILE = 0;
    private static final Integer TEMP_BRANCH_OTHER_FILE = 1;

    /**
     * 组织类型
     */
    private static final Integer BRANCH_TYPE = 0;
    private static final Integer TEMP_BRANCH_TYPE = 1;

    private static final String BRANCH_NAME = "组务会";
    private static final String TEMP_BRANCH_NAME = "临时党支部会";

    /**
     * 统计巡视方案下被巡视单位InspectionOrganizationMeetings次数
     * *
     *
     * @param planId           巡视计划ID
     * @param organizationType 组织类型
     */
    public List<UnitsBizCountVO> selectInspectionOrganizationMeetingsCount(String planId, Integer organizationType) throws BaseException {
        List<UnitsBizCountVO> unitsBizCountVOS = inspectionOrganizationMeetingsMapper.selectInspectionOrganizationMeetingsCount(planId, organizationType);
        if (!CollectionUtils.isEmpty(unitsBizCountVOS)) {
            unitsBizCountVOS.forEach(var -> {
                Integer currentOrganizationType = var.getOrganizationType();
                if (!organizationType.equals(currentOrganizationType)) {
                    var.setCount(0);
                }
            });
        }
        unitsBizCountVOS.removeIf(unitsBizCountVO -> (null != unitsBizCountVO.getOrganizationType()) && (!unitsBizCountVO.getOrganizationType().equals(organizationType)));
        return unitsBizCountVOS;
    }

    /**
     * 查询组织会议列表
     *
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    public List<InspectionOrganizationMeetingsPO> selectInspectionOrganizationMeetingsList(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        QueryWrapper<InspectionOrganizationMeetingsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionOrganizationMeetings.getUnitsId());
        queryWrapper.eq("plan_id", inspectionOrganizationMeetings.getPlanId());
        queryWrapper.eq("ORGANIZATION_TYPE", inspectionOrganizationMeetings.getOrganizationType());
        return inspectionOrganizationMeetingsMapper.selectList(queryWrapper);
    }

    /**
     * 新增组织会议
     *
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    public int save(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        int result = inspectionOrganizationMeetingsMapper.insert(inspectionOrganizationMeetings);
        List<PiisDocumentPO> documents = inspectionOrganizationMeetings.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                Long dictId = getFileDictId(inspectionOrganizationMeetings.getOrganizationType());
                documentService.updateDocumentById(document.setObjectId("OrganizationMeetings" + inspectionOrganizationMeetings.getOrganizationMeetingsId()).setFileDictId(dictId));
            }
        }
        return result;
    }

    /**
     * 根据ID修改组织会议
     *
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    public int update(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        List<PiisDocumentPO> documents = inspectionOrganizationMeetings.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            Integer organizationType = inspectionOrganizationMeetings.getOrganizationType();
                            Long dictId = getFileDictId(organizationType);
                            document.setFileDictId(dictId)
                                    .setObjectId("OrganizationMeetings" + inspectionOrganizationMeetings.getOrganizationMeetingsId());
                            documentService.updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            documentService.deleteDocumentById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return inspectionOrganizationMeetingsMapper.updateById(inspectionOrganizationMeetings);
    }

    /**
     * 根据ID批量删除组织会议
     *
     * @param organizationMeetingsIds 组织会议编号
     * @return
     */
    public int deleteByInspectionOrganizationMeetingsIds(Long[] organizationMeetingsIds) {
        List<Long> list = Arrays.asList(organizationMeetingsIds);
        return inspectionOrganizationMeetingsMapper.deleteBatchIds(list);
    }

    /**
     * 获取文件类型ID
     *
     * @param orgType
     * @return
     */
    private Long getFileDictId(Integer orgType) {
        Long dictId = null;
        if (TEMP_ORG_OTHER_FILE.equals(orgType)) {
            dictId = FileEnum.ORG_OTHER_FILE.getCode();
        } else if (TEMP_BRANCH_OTHER_FILE.equals(orgType)) {
            dictId = FileEnum.BRANCH_OTHER_FILE.getCode();
        }
        return dictId;
    }


    /**
     * 新增代办
     *
     * @param organizationMeetings
     */
    private void handleTodo(InspectionOrganizationMeetingsPO organizationMeetings) {
        String planId = organizationMeetings.getPlanId();
        Long unitsId = organizationMeetings.getUnitsId();
        PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                .setPlanId(planId)
                .setUnitsId(unitsId);
        PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
        String formName;
        if (BRANCH_TYPE.equals(organizationMeetings.getOrganizationType())) {
            formName = BRANCH_NAME;
        } else {
            formName = TEMP_BRANCH_NAME;
        }
        WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                .setLookStatus(NO_LOOK)
                .setTodoName("[" + formName + "]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                .setBusinessId(String.valueOf(organizationMeetings.getOrganizationMeetingsId()))
                .setTodoStatus(TODO_NEED)
                .setApproverId(organizationMeetings.getApproverId())
                .setApproverName(organizationMeetings.getApproverName())
                .setRemark(formName);
        BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
        todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
    }

    /**
     * 审批
     *
     * @param organizationMeetingsList
     */
    @Override
    public void doApprovals(List<InspectionOrganizationMeetingsPO> organizationMeetingsList) {
        if (!CollectionUtils.isEmpty(organizationMeetingsList)) {
            organizationMeetingsList.forEach(organizationMeetings -> {
                organizationMeetings.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                inspectionOrganizationMeetingsMapper.updateById(organizationMeetings.setDocuments(null));
                handleTodo(organizationMeetings);
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
        log.info("###ApplicationListener notify event [组务会/支部会]###");
        Object object = event.getSource();
        if (object instanceof InspectionOrganizationMeetingsServiceImpl) {
            InspectionOrganizationMeetingsPO inspectionConsultInfo = new InspectionOrganizationMeetingsPO()
                    .setOrganizationMeetingsId(Long.valueOf(event.getBizId()));
            String remark = event.getRemark();
            if(BRANCH_NAME.equals(remark)){
                inspectionConsultInfo.setOrganizationType(BRANCH_TYPE);
            }else if(TEMP_BRANCH_NAME.equals(remark)){
                inspectionConsultInfo.setOrganizationType(TEMP_BRANCH_TYPE);
            }
            Integer eventType = event.getEventType();
            if (OperationEnum.SELECT.getCode() == eventType) {
                InspectionOrganizationMeetingsPO result = inspectionOrganizationMeetingsMapper.selectOrganizationMeetingsWithFile(inspectionConsultInfo);
                event.setData(result);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        inspectionConsultInfo.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        inspectionConsultInfo.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    inspectionConsultInfo.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionOrganizationMeetingsPO.class, inspectionConsultInfo);
                inspectionOrganizationMeetingsMapper.updateById(inspectionConsultInfo.setDocuments(null));
            }
        }
    }
}