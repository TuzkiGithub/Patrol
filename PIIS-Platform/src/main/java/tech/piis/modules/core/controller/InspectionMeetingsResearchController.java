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
import tech.piis.modules.core.domain.po.InspectionMeetingsResearchPO;
import tech.piis.modules.core.service.IInspectionMeetingsResearchService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 会议研究Controller
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@RestController
@RequestMapping("/piis/research")
public class InspectionMeetingsResearchController extends BaseController {
    @Autowired
    private IInspectionMeetingsResearchService inspectionMeetingsResearchService;

    /**
     * 查询会议研究列表
     *
     * @param inspectionMeetingsResearch
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException {
        startPage();
        List<InspectionMeetingsResearchPO> data = inspectionMeetingsResearchService.selectInspectionMeetingsResearchList(inspectionMeetingsResearch);
        return getDataTable(data);
    }

    /**
     * 查询会议研究总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionMeetingsResearchList(String planId) throws BaseException {
        return AjaxResult.success(inspectionMeetingsResearchService.selectInspectionMeetingsResearchCount(planId));
    }

    /**
     * 新增会议研究
     *
     * @param inspectionMeetingsResearch
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "会议研究", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionMeetingsResearchPO inspectionMeetingsResearch) {
        if (null == inspectionMeetingsResearch) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionMeetingsResearch.getIsApproval()) {
            inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionMeetingsResearchPO.class, inspectionMeetingsResearch);
        return toAjax(inspectionMeetingsResearchService.save(inspectionMeetingsResearch));
    }

    /**
     * 审批会议研究
     *
     * @param inspectionMeetingsResearchList
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "会议研究", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionMeetingsResearchPO> inspectionMeetingsResearchList) {
        if (CollectionUtils.isEmpty(inspectionMeetingsResearchList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionMeetingsResearchService.doApprovals(inspectionMeetingsResearchList);
        return AjaxResult.success();
    }

    /**
     * 修改会议研究
     *
     * @param inspectionMeetingsResearch
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "会议研究", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException {
        if (null == inspectionMeetingsResearch) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionMeetingsResearch.getIsApproval()) {
            inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionMeetingsResearch.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionMeetingsResearchPO.class, inspectionMeetingsResearch);
        return toAjax(inspectionMeetingsResearchService.update(inspectionMeetingsResearch));
    }

    /**
     * 删除会议研究
     * meetingsResearchIds 会议研究ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolReport:perms')")
    @Log(title = "会议研究", businessType = BusinessType.DELETE)
    @DeleteMapping("/{meetingsResearchIds}")
    public AjaxResult remove(@PathVariable Long[] meetingsResearchIds) throws BaseException {
        return toAjax(inspectionMeetingsResearchService.deleteByInspectionMeetingsResearchIds(meetingsResearchIds));
    }
}
