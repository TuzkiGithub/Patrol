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
import tech.piis.modules.core.domain.po.InspectionVisitPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionVisitService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 来访Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/visit")
public class InspectionVisitController extends BaseController {
    @Autowired
    private IInspectionVisitService inspectionVisitService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 与前端接口定义文件类型
     */
    private static final Long TEMP_VISIT_OTHER_FILE = 1L;
    private static final Long TEMP_VISIT_SEAL_FILE = 0L;

    /**
     * 查询来访列表
     *
     * @param inspectionVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionVisitPO inspectionVisit) throws BaseException {
        startPage();
        List<InspectionVisitPO> data = inspectionVisitService.selectInspectionVisitList(inspectionVisit);
        return getDataTable(data);
    }

    /**
     * 查询来访文件
     *
     * @param visitId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/file")
    public AjaxResult findInspectionVisitFile(String visitId) throws BaseException {
        List<PiisDocumentPO> documents = documentService.getFileListByBizId("Visit" + visitId);
        convertTempDict(documents);
        return AjaxResult.success(documents);
    }

    /**
     * 查询来访总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionVisitList(String planId) throws BaseException {
        return AjaxResult.success(inspectionVisitService.selectInspectionVisitCount(planId));
    }

    /**
     * 新增来访
     *
     * @param inspectionVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "来访", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionVisitPO inspectionVisit) {
        if (null == inspectionVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionVisit.getIsApproval()) {
            inspectionVisit.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionVisit.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        convertFileDict(inspectionVisit);
        BizUtils.setCreatedOperation(InspectionVisitPO.class, inspectionVisit);
        return toAjax(inspectionVisitService.save(inspectionVisit));
    }

    /**
     * 审批来访
     *
     * @param inspectionVisitList
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "来访", businessType = BusinessType.INSERT)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionVisitPO> inspectionVisitList) {
        if (CollectionUtils.isEmpty(inspectionVisitList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionVisitService.doApprovals(inspectionVisitList);
        return AjaxResult.success();
    }

    /**
     * 修改来访
     *
     * @param inspectionVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "来访", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionVisitPO inspectionVisit) throws BaseException {
        if (null == inspectionVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionVisit.getIsApproval()) {
            inspectionVisit.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionVisit.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        convertFileDict(inspectionVisit);
        BizUtils.setUpdatedOperation(InspectionVisitPO.class, inspectionVisit);
        return toAjax(inspectionVisitService.update(inspectionVisit));
    }

    /**
     * 删除来访
     * callVisitIds 来访ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "来访", businessType = BusinessType.DELETE)
    @DeleteMapping("/{callVisitIds}")
    public AjaxResult remove(@PathVariable String[] callVisitIds) throws BaseException {
        return toAjax(inspectionVisitService.deleteByInspectionVisitIds(callVisitIds));
    }

    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionVisit
     */
    private void convertFileDict(InspectionVisitPO inspectionVisit) {
        List<PiisDocumentPO> documents = inspectionVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_VISIT_OTHER_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.VISIT_OTHER_FILE.getCode());
                } else if (TEMP_VISIT_SEAL_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.VISIT_SEAL_FILE.getCode());
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
                if (FileEnum.VISIT_OTHER_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_VISIT_OTHER_FILE);
                } else if (FileEnum.VISIT_SEAL_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_VISIT_SEAL_FILE);
                }
            });
        }
    }
}