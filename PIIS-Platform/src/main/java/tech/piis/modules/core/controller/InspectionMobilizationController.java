package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionMobilizationPO;
import tech.piis.modules.core.service.IInspectionMobilizationService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 巡视动员会Controller
 *
 * @author Tuzki
 * @date 2020-11-25
 */
@RestController
@RequestMapping("/piis/mobilization")
public class InspectionMobilizationController extends BaseController {
    @Autowired
    private IInspectionMobilizationService inspectionMobilizationService;

    /**
     * 查询巡视动员会列表
     *
     * @param inspectionMobilization
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping()
    public AjaxResult list(InspectionMobilizationPO inspectionMobilization) throws BaseException {
        List<InspectionMobilizationPO> mobilizationPOList = inspectionMobilizationService.selectInspectionMobilizationList(inspectionMobilization);
        return AjaxResult.success(mobilizationPOList);
    }

    /**
     * 新增巡视动员会
     *
     * @param inspectionMobilization
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionMobilizationPO inspectionMobilization) {
        if (null == inspectionMobilization) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionMobilizationPO.class, inspectionMobilization);
        return toAjax(inspectionMobilizationService.save(inspectionMobilization));
    }

    /**
     * 保存巡视动员会
     *
     * @param inspectionMobilization
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.INSERT)
    @PostMapping("preservation")
    public AjaxResult preservation(@RequestBody @Valid InspectionMobilizationPO inspectionMobilization) {
        inspectionMobilization.setSubmitFlag(false);
        inspectionMobilization.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        if (NO_APPROVAL == inspectionMobilization.getIsApproval()) {
            inspectionMobilization.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        }
        if (OperationEnum.INSERT.getCode() == inspectionMobilization.getSaveFlag()) {
            return add(inspectionMobilization);
        } else if (OperationEnum.UPDATE.getCode() == inspectionMobilization.getSaveFlag()) {
            return edit(inspectionMobilization);
        }
        return AjaxResult.error();
    }

    /**
     * 提交巡视动员会
     *
     * @param inspectionMobilization
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.INSERT)
    @PostMapping("submit")
    public AjaxResult submit(@RequestBody @Valid InspectionMobilizationPO inspectionMobilization) {
        inspectionMobilization.setSubmitFlag(true);
        inspectionMobilization.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
        if (NO_APPROVAL == inspectionMobilization.getIsApproval()) {
            inspectionMobilization.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        }
        if (OperationEnum.INSERT.getCode() == inspectionMobilization.getSaveFlag()) {
            return add(inspectionMobilization);
        } else if (OperationEnum.UPDATE.getCode() == inspectionMobilization.getSaveFlag()) {
            return edit(inspectionMobilization);
        }
        return AjaxResult.error();
    }

    /**
     * 修改巡视动员会
     *
     * @param inspectionMobilization
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionMobilizationPO inspectionMobilization) throws BaseException {
        if (null == inspectionMobilization) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionMobilizationPO.class, inspectionMobilization);
        return toAjax(inspectionMobilizationService.update(inspectionMobilization));
    }

    /**
     * 删除巡视动员会
     * mobilizationIds 巡视动员会ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.DELETE)
    @DeleteMapping("/{mobilizationIds}")
    public AjaxResult remove(@PathVariable Long[] mobilizationIds) throws BaseException {
        return toAjax(inspectionMobilizationService.deleteByInspectionMobilizationIds(mobilizationIds));
    }

}