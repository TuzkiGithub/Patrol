package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionProblemDraftPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionProblemDraftService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 问题底稿 Controller
 *
 * @author Kevin
 * @date 2020-10-23
 */
@RestController
@RequestMapping("/piis/draft")
public class InspectionProblemDraftController extends BaseController {
    @Autowired
    private IInspectionProblemDraftService inspectionProblemDraftService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 文件映射
     */
    private static final Long TEMP_PIIS_INFORMATION_FILE = 1L;
    private static final Long TEMP_SUPPORTING_MATERIALS_FILE = 2L;
    private static final Long TEMP_PROBLEM_DRAFT_FILE = 3L;

    /**
     * 查询问题底稿 列表
     *
     * @param inspectionProblemDraft
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        startPage();
        List<InspectionProblemDraftPO> data = inspectionProblemDraftService.selectInspectionProblemDraftList(inspectionProblemDraft);
        return getDataTable(data);
    }

    /**
     * 查询问题底稿 文件
     *
     * @param inspectionProblemDraftId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/file")
    public AjaxResult findInspectionProblemDraftFile(@RequestParam("problemDraftId") String inspectionProblemDraftId) throws BaseException {
        List<PiisDocumentPO> documents = documentService.getFileListByBizId("ProblemDraft" + inspectionProblemDraftId);
        convertTempDict(documents);
        return AjaxResult.success(documents);
    }

    /**
     * 查询问题底稿 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionProblemDraftList(String planId) throws BaseException {
        return AjaxResult.success(inspectionProblemDraftService.selectInspectionProblemDraftCount(planId));
    }

    /**
     * 新增问题底稿
     *
     * @param inspectionProblemDraft
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "问题底稿 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionProblemDraftPO inspectionProblemDraft) {
        if (null == inspectionProblemDraft) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionProblemDraft.getIsApproval()) {
            inspectionProblemDraft.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionProblemDraft.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        convertFileDict(inspectionProblemDraft);
        BizUtils.setCreatedOperation(InspectionProblemDraftPO.class, inspectionProblemDraft);
        return toAjax(inspectionProblemDraftService.save(inspectionProblemDraft));
    }

    /**
     * 审批问题底稿
     *
     * @param inspectionProblemDraftPOList
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "问题底稿", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionProblemDraftPO> inspectionProblemDraftPOList) {
        if (CollectionUtils.isEmpty(inspectionProblemDraftPOList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionProblemDraftService.doApprovals(inspectionProblemDraftPOList);
        return AjaxResult.success();
    }
    /**
     * 修改问题底稿
     *
     * @param inspectionProblemDraft
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "问题底稿 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        if (null == inspectionProblemDraft) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionProblemDraft.getIsApproval()) {
            inspectionProblemDraft.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionProblemDraft.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        convertFileDict(inspectionProblemDraft);
        BizUtils.setUpdatedOperation(InspectionProblemDraftPO.class, inspectionProblemDraft);
        return toAjax(inspectionProblemDraftService.update(inspectionProblemDraft));
    }

    /**
     * 删除问题底稿
     * problemDraftIds 问题底稿 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "问题底稿 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{problemDraftIds}")
    public AjaxResult remove(@PathVariable Long[] problemDraftIds) throws BaseException {
        return toAjax(inspectionProblemDraftService.deleteByInspectionProblemDraftIds(problemDraftIds));
    }


    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionProblemDraft
     */
    private void convertFileDict(InspectionProblemDraftPO inspectionProblemDraft) {
        List<PiisDocumentPO> documents = inspectionProblemDraft.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_PIIS_INFORMATION_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PIIS_INFORMATION_FILE.getCode());
                } else if (TEMP_SUPPORTING_MATERIALS_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.SUPPORTING_MATERIALS_FILE.getCode());
                } else if (TEMP_PROBLEM_DRAFT_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PROBLEM_DRAFT_FILE.getCode());
                }
            });
        }
    }


    /**
     * 实际文件字典 -》 临时文件字典
     *
     * @param documents
     */
    private void convertTempDict(List<PiisDocumentPO> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long DictId = document.getFileDictId();
                if (FileEnum.PIIS_INFORMATION_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_PIIS_INFORMATION_FILE);
                } else if (FileEnum.SUPPORTING_MATERIALS_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_SUPPORTING_MATERIALS_FILE);
                } else if (FileEnum.PROBLEM_DRAFT_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_PROBLEM_DRAFT_FILE);
                }
            });
        }
    }

}
