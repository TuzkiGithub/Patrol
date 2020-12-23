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
import tech.piis.modules.core.domain.po.InspectionFeedbackMeetingsPO;
import tech.piis.modules.core.service.IInspectionFeedbackMeetingsService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 反馈会Controller
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@RestController
@RequestMapping("/piis/feedback/meetings")
public class InspectionFeedbackMeetingsController extends BaseController {
    @Autowired
    private IInspectionFeedbackMeetingsService inspectionFeedbackMeetingsService;

    /**
     * 查询反馈会列表
     *
     * @param inspectionFeedbackMeetings
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException {
        startPage();
        List<InspectionFeedbackMeetingsPO> data = inspectionFeedbackMeetingsService.selectInspectionFeedbackMeetingsList(inspectionFeedbackMeetings);
        return getDataTable(data);
    }

    /**
     * 查询反馈会 览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionFeedbackList(String planId) throws BaseException {
        return AjaxResult.success(inspectionFeedbackMeetingsService.selectInspectionFeedbackMeetingsCount(planId));
    }

    /**
     * 新增反馈会
     *
     * @param inspectionFeedbackMeetings
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈会", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) {
        if (null == inspectionFeedbackMeetings) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionFeedbackMeetings.getIsApproval()) {
            inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionFeedbackMeetingsPO.class, inspectionFeedbackMeetings);
        return toAjax(inspectionFeedbackMeetingsService.save(inspectionFeedbackMeetings));
    }

    /**
     * 审批反馈会
     *
     * @param inspectionFeedbackMeetingsList
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈会", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionFeedbackMeetingsPO> inspectionFeedbackMeetingsList) {
        if (CollectionUtils.isEmpty(inspectionFeedbackMeetingsList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionFeedbackMeetingsService.doApprovals(inspectionFeedbackMeetingsList);
        return AjaxResult.success();
    }

    /**
     * 修改反馈会
     *
     * @param inspectionFeedbackMeetings
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈会", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException {
        if (null == inspectionFeedbackMeetings) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionFeedbackMeetings.getIsApproval()) {
            inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionFeedbackMeetings.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionFeedbackMeetingsPO.class, inspectionFeedbackMeetings);
        return toAjax(inspectionFeedbackMeetingsService.update(inspectionFeedbackMeetings));
    }

    /**
     * 删除反馈会
     * feedbackMeetingsIds 反馈会ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈会", businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackMeetingsIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackMeetingsIds) throws BaseException {
        return toAjax(inspectionFeedbackMeetingsService.deleteByInspectionFeedbackMeetingsIds(feedbackMeetingsIds));
    }
}
