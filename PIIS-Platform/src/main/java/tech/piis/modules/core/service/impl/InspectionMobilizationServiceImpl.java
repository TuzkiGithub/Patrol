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
import tech.piis.modules.core.domain.po.InspectionMobilizationPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.core.mapper.InspectionMobilizationMapper;
import tech.piis.modules.core.service.IInspectionMobilizationService;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tech.piis.common.constant.OperationConstants.INSERT;
import static tech.piis.common.constant.PiisConstants.*;

/**
 * 巡视动员会Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-25
 */
@Slf4j
@Transactional
@Service
public class InspectionMobilizationServiceImpl implements IInspectionMobilizationService, ApplicationListener<WorkFlowEvent> {
    @Autowired
    private InspectionMobilizationMapper inspectionMobilizationMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Autowired
    private IWfWorkflowTodoService todoService;

    @Autowired
    private IInspectionPlanService planService;

    /**
     * 与前端接口定义文件类型
     */
    private static final Map<Long, Long> TO_DICT_MAP = new HashMap<>();
    private static final Map<Long, Long> TO_TEMP_MAP = new HashMap<>();

    static {
        TO_DICT_MAP.put(1L, FileEnum.AGENDA_FILE.getCode());
        TO_DICT_MAP.put(2L, FileEnum.SECRETARY_SPEECH_FILE.getCode());
    }

    static {
        TO_TEMP_MAP.put(FileEnum.AGENDA_FILE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.SECRETARY_SPEECH_FILE.getCode(), 2L);
    }

    /**
     * 查询巡视动员会列表
     *
     * @param inspectionMobilization
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionMobilizationPO> selectInspectionMobilizationList(InspectionMobilizationPO inspectionMobilization) throws BaseException {
        QueryWrapper<InspectionMobilizationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionMobilization.getPlanId());
        queryWrapper.eq("UNITS_ID", inspectionMobilization.getUnitsId());
        List<InspectionMobilizationPO> mobilizationList = inspectionMobilizationMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(mobilizationList)) {
            mobilizationList.forEach(mobilization -> {
                List<PiisDocumentPO> documents = documentService.getFileListByBizId("InspectionMobilization" + mobilization.getMobilizationId());
                mobilization.setRangeList(BizUtils.paramsCovert2List(mobilization.getRangeId(),mobilization.getRangeName()));
                BizUtils.convertFileDict(inspectionMobilization.getDocuments(), TO_TEMP_MAP);
                mobilization.setDocuments(documents);
            });
        }
        return mobilizationList;
    }

    /**
     * 新增巡视动员会
     *
     * @param inspectionMobilization
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionMobilizationPO inspectionMobilization) throws BaseException {
        BizUtils.convertFileDict(inspectionMobilization.getDocuments(), TO_DICT_MAP);
        int result = inspectionMobilizationMapper.insert(inspectionMobilization);
        List<PiisDocumentPO> documents = inspectionMobilization.getDocuments();
        if (OperationEnum.INSERT.getCode() == inspectionMobilization.getSaveFlag()) {
            if (!CollectionUtils.isEmpty(documents)) {
                documents.forEach(document -> document.setOperationType(INSERT));
            }
        }
        Object bizId = inspectionMobilization.getMobilizationId();
        documentService.updateDocumentBatch(documents, "InspectionMobilization" + bizId);
        handleTodo(inspectionMobilization);
        return result;
    }

    /**
     * 根据ID修改巡视动员会
     *
     * @param inspectionMobilization
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionMobilizationPO inspectionMobilization) throws BaseException {
        BizUtils.convertFileDict(inspectionMobilization.getDocuments(), TO_DICT_MAP);
        handleTodo(inspectionMobilization);
        Object bizId = inspectionMobilization.getMobilizationId();
        documentService.updateDocumentBatch(inspectionMobilization.getDocuments(), "InspectionMobilization" + bizId);
        return inspectionMobilizationMapper.updateById(inspectionMobilization);
    }

    /**
     * 根据ID批量删除巡视动员会
     *
     * @param mobilizationIds 巡视动员会编号
     * @return
     */
    @Override
    public int deleteByInspectionMobilizationIds(Long[] mobilizationIds) {
        List<Long> list = Arrays.asList(mobilizationIds);
        return inspectionMobilizationMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionMobilizationMapper.selectCount(null);
    }

    /**
     * 新增代办
     *
     * @param inspectionMobilization
     */
    private void handleTodo(InspectionMobilizationPO inspectionMobilization) {
        if (inspectionMobilization.isSubmitFlag()) {
            String planId = inspectionMobilization.getPlanId();
            Long unitsId = inspectionMobilization.getUnitsId();
            PlanBriefDTO planBriefDTO = new PlanBriefDTO()
                    .setPlanId(planId)
                    .setUnitsId(unitsId);
            PlanBriefVO planPO = planService.selectPiisBrief(planBriefDTO);
            WfWorkFlowTodoPO wfWorkFlowTodoPO = new WfWorkFlowTodoPO()
                    .setLookStatus(NO_LOOK)
                    .setTodoName("[巡视动员会]-" + planPO.getPlanName() + "-" + planPO.getGroupName() + "-" + planPO.getOrgName())
                    .setBusinessId(String.valueOf(inspectionMobilization.getMobilizationId()))
                    .setTodoStatus(TODO_NEED)
                    .setApproverId(inspectionMobilization.getApproverId())
                    .setApproverName(inspectionMobilization.getApproverName());
            BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, wfWorkFlowTodoPO);
            todoService.saveWorkflowTodo(wfWorkFlowTodoPO);
        }
    }


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(WorkFlowEvent event) {
        System.out.println("###ApplicationListener notify event [巡视动员会]###");
        log.info("###ApplicationListener notify event [巡视动员会]###");
        Object object = event.getSource();
        if (object instanceof InspectionMobilizationServiceImpl) {
            Integer eventType = event.getEventType();
            if (OperationEnum.SELECT.getCode() == eventType) {
                QueryWrapper<InspectionMobilizationPO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("MOBILIZATION_ID", event.getBizId());
                InspectionMobilizationPO mobilization = inspectionMobilizationMapper.selectOne(queryWrapper);
                List<PiisDocumentPO> documents = documentService.getFileListByBizId("InspectionMobilization" + event.getBizId());
                //设置文件类型
                BizUtils.convertFileDict(documents, TO_TEMP_MAP);
                mobilization.setDocuments(documents);
                //设置人员字段
                mobilization.setRangeList(BizUtils.paramsCovert2List(mobilization.getRangeId(), mobilization.getRangeName()));
                event.setData(mobilization);
            } else if (OperationEnum.UPDATE.getCode() == eventType) {
                Integer continueApprovalFlag = event.getContinueApprovalFlag();
                Integer agreeFlag = event.getAgreeFlag();
                InspectionMobilizationPO mobilizationPO = new InspectionMobilizationPO()
                        .setMobilizationId(Long.valueOf(event.getBizId()));
                if (NO_APPROVAL == continueApprovalFlag) {
                    if (AGREE == agreeFlag) {
                        mobilizationPO.setApprovalFlag(ApprovalEnum.PASSED.getCode());
                    } else {
                        mobilizationPO.setApprovalFlag(ApprovalEnum.REJECTED.getCode());
                    }
                } else {
                    mobilizationPO.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
                }
                BizUtils.setUpdatedOperation(InspectionMobilizationPO.class, mobilizationPO);
                inspectionMobilizationMapper.updateById(mobilizationPO);
            }
        }
    }
}