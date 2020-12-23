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
import tech.piis.modules.core.domain.po.InspectionPatrolReportPO;
import tech.piis.modules.core.service.IInspectionPatrolReportService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 巡视报告Controller
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@RestController
@RequestMapping("/piis/patrol/report")
public class InspectionPatrolReportController extends BaseController {
    @Autowired
    private IInspectionPatrolReportService inspectionPatrolReportService;
    

    /**
     * 查询巡视报告列表
     *
     * @param inspectionPatrolReport
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException {
        startPage();
        List<InspectionPatrolReportPO> data = inspectionPatrolReportService.selectInspectionPatrolReportList(inspectionPatrolReport);
        return getDataTable(data);
    }

    /**
     * 查询巡视报告总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionPatrolReportList(String planId) throws BaseException {
        return AjaxResult.success(inspectionPatrolReportService.selectInspectionPatrolReportCount(planId));
    }

    /**
     * 新增巡视报告
     *
     * @param inspectionPatrolReport
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "巡视报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionPatrolReportPO inspectionPatrolReport) {
        if (null == inspectionPatrolReport) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionPatrolReport.getIsApproval()) {
            inspectionPatrolReport.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionPatrolReport.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionPatrolReportPO.class, inspectionPatrolReport);
        return toAjax(inspectionPatrolReportService.save(inspectionPatrolReport));
    }

    /**
     * 审批巡视报告
     *
     * @param inspectionPatrolReportList
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "巡视报告", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionPatrolReportPO> inspectionPatrolReportList) {
        if (CollectionUtils.isEmpty(inspectionPatrolReportList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionPatrolReportService.doApprovals(inspectionPatrolReportList);
        return AjaxResult.success();
    }

    /**
     * 修改巡视报告
     *
     * @param inspectionPatrolReport
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "巡视报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionPatrolReportPO inspectionPatrolReport) throws BaseException {
        if (null == inspectionPatrolReport) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionPatrolReport.getIsApproval()) {
            inspectionPatrolReport.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionPatrolReport.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionPatrolReportPO.class, inspectionPatrolReport);
        return toAjax(inspectionPatrolReportService.update(inspectionPatrolReport));
    }

    /**
     * 删除巡视报告
     * patrolReportIds 巡视报告ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "巡视报告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{patrolReportIds}")
    public AjaxResult remove(@PathVariable Long[] patrolReportIds) throws BaseException {
        return toAjax(inspectionPatrolReportService.deleteByInspectionPatrolReportIds(patrolReportIds));
    }

}
