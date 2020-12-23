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
import tech.piis.modules.core.domain.po.InspectionRectificationPO;
import tech.piis.modules.core.service.IInspectionRectificationService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 整改公开情况Controller
 *
 * @author Tuzki
 * @date 2020-12-11
 */
@RestController
@RequestMapping("/piis/rectification")
public class InspectionRectificationController extends BaseController {
    @Autowired
    private IInspectionRectificationService inspectionRectificationService;

    /**
     * 查询整改公开情况列表
     *
     * @param inspectionRectification
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolRectification:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionRectificationPO inspectionRectification) throws BaseException {
        startPage();
        List<InspectionRectificationPO> data = inspectionRectificationService.selectInspectionRectificationList(inspectionRectification);
        return getDataTable(data);
    }

    /**
     * 查询整改公开情况总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolRectification:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionRectificationList(String planId) throws BaseException {
        return AjaxResult.success(inspectionRectificationService.selectInspectionRectificationCount(planId));
    }

    /**
     * 新增整改公开情况
     *
     * @param inspectionRectification
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolRectification:perms')")
    @Log(title = "整改公开情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionRectificationPO inspectionRectification) {
        if (null == inspectionRectification) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionRectification.getIsApproval()) {
            inspectionRectification.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionRectification.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionRectificationPO.class, inspectionRectification);
        return toAjax(inspectionRectificationService.save(inspectionRectification));
    }

    /**
     * 审批整改公开情况
     *
     * @param inspectionRectificationList
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolRectification:perms')")
    @Log(title = "整改公开情况", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionRectificationPO> inspectionRectificationList) {
        if (CollectionUtils.isEmpty(inspectionRectificationList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionRectificationService.doApprovals(inspectionRectificationList);
        return AjaxResult.success();
    }

    /**
     * 修改整改公开情况
     *
     * @param inspectionRectification
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolRectification:perms')")
    @Log(title = "整改公开情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionRectificationPO inspectionRectification) throws BaseException {
        if (null == inspectionRectification) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionRectification.getIsApproval()) {
            inspectionRectification.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionRectification.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionRectificationPO.class, inspectionRectification);
        return toAjax(inspectionRectificationService.update(inspectionRectification));
    }

    /**
     * 删除整改公开情况
     * rectificationIds 整改公开情况ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:patrolRectification:perms')")
    @Log(title = "整改公开情况", businessType = BusinessType.DELETE)
    @DeleteMapping("/{rectificationIds}")
    public AjaxResult remove(@PathVariable Long[] rectificationIds) throws BaseException {
        return toAjax(inspectionRectificationService.deleteByInspectionRectificationIds(rectificationIds));
    }
}
