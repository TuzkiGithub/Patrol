package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionImportantReportPO;
import tech.piis.modules.core.service.IInspectionImportantReportService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 重要情况专题报告 Controller
 *
 * @author Kevin
 * @date 2020-10-23
 */
@RestController
@RequestMapping("/piis/import/report")
public class InspectionImportantReportController extends BaseController {
    @Autowired
    private IInspectionImportantReportService inspectionImportantReportService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询重要情况专题报告 列表
     *
     * @param inspectionImportantReport
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        startPage();
        List<InspectionImportantReportPO> data = inspectionImportantReportService.selectInspectionImportantReportList(inspectionImportantReport);
        return getDataTable(data);
    }

    /**
     * 查询重要情况专题报告 文件
     *
     * @param inspectionImportantReportId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/file")
    public AjaxResult findInspectionImportantReportFile(@RequestParam("importantReportId") String inspectionImportantReportId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("ImportantReport" + inspectionImportantReportId));
    }

    /**
     * 查询重要情况专题报告 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionImportantReportList(String planId) throws BaseException {
        return AjaxResult.success(inspectionImportantReportService.selectInspectionImportantReportCount(planId));
    }

    /**
     * 新增重要情况专题报告
     *
     * @param inspectionImportantReport
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "重要情况专题报告 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionImportantReportPO inspectionImportantReport) {
        if (null == inspectionImportantReport) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionImportantReport.getIsApproval()) {
            inspectionImportantReport.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionImportantReport.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionImportantReportPO.class, inspectionImportantReport);
        return toAjax(inspectionImportantReportService.save(inspectionImportantReport));
    }

    /**
     * 审批重要情况专题报告
     *
     * @param inspectionImportantReportList
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "重要情况专题报告 ", businessType = BusinessType.INSERT)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionImportantReportPO> inspectionImportantReportList) {
        if (CollectionUtils.isEmpty(inspectionImportantReportList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionImportantReportService.doApprovals(inspectionImportantReportList);
        return AjaxResult.success();
    }

    /**
     * 修改重要情况专题报告
     *
     * @param inspectionImportantReport
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "重要情况专题报告 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        if (null == inspectionImportantReport) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionImportantReport.getIsApproval()) {
            inspectionImportantReport.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionImportantReport.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionImportantReportPO.class, inspectionImportantReport);
        return toAjax(inspectionImportantReportService.update(inspectionImportantReport));
    }

    /**
     * 删除重要情况专题报告
     * inspectionImportantReportIds 重要情况专题报告 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "重要情况专题报告 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{inspectionImportantReportIds}")
    public AjaxResult remove(@PathVariable Long[] inspectionImportantReportIds) throws BaseException {
        return toAjax(inspectionImportantReportService.deleteByInspectionImportantReportIds(inspectionImportantReportIds));
    }

}