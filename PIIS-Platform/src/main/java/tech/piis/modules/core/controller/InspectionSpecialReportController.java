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
import tech.piis.modules.core.domain.po.InspectionSpecialReportPO;
import tech.piis.modules.core.service.IInspectionSpecialReportService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;

/**
 * 听取专题报告 Controller
 *
 * @author Kevin
 * @date 2020-10-12
 */
@RestController
@RequestMapping("/piis/report")
public class InspectionSpecialReportController extends BaseController {
    @Autowired
    private IInspectionSpecialReportService inspectionSpecialReportService;


    /**
     * 查询听取专题报告 列表
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionSpecialReportPO inspectionSpecialReport) {
        startPage();
        List<InspectionSpecialReportPO> data = inspectionSpecialReportService.selectSpecialReport(inspectionSpecialReport);
        return getDataTable(data);
    }


    /**
     * 查询听取专题报告总览列表
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countList(InspectionSpecialReportPO inspectionSpecialReport) {
        return AjaxResult.success(inspectionSpecialReportService.selectSpecialReportCount(inspectionSpecialReport.getPlanId())
        );
    }


    /**
     * 新增听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionSpecialReportPO inspectionSpecialReport) throws BaseException {
        if (null == inspectionSpecialReport) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionSpecialReport.getIsApproval()) {
            inspectionSpecialReport.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionSpecialReport.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionSpecialReportPO.class, inspectionSpecialReport);
        return toAjax(inspectionSpecialReportService.save(inspectionSpecialReport));
    }

    /**
     * 审批听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionSpecialReportPO> inspectionSpecialReportPOList) throws BaseException {
        if (CollectionUtils.isEmpty(inspectionSpecialReportPOList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionSpecialReportService.doApprovals(inspectionSpecialReportPOList);
        return AjaxResult.success();
    }

    /**
     * 修改听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionSpecialReportPO inspectionSpecialReport) throws BaseException {
        if (null == inspectionSpecialReport) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionSpecialReport.getIsApproval()) {
            inspectionSpecialReport.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionSpecialReport.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionSpecialReportPO.class, inspectionSpecialReport);
        return toAjax(inspectionSpecialReportService.update(inspectionSpecialReport));
    }

    /**
     * 删除听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{specialReportIds}")
    public AjaxResult remove(@PathVariable Long[] specialReportIds) {
        return toAjax(inspectionSpecialReportService.deleteBySpecialReportIds(specialReportIds));
    }
}
