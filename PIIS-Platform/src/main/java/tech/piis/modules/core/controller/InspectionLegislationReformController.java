package tech.piis.modules.core.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionLegislationReformPO;
import tech.piis.modules.core.service.IInspectionLegislationReformService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 立行立改 Controller
 *
 * @date 2020-10-23
 */
@RestController
@RequestMapping("/piis/legislation/reform")
public class InspectionLegislationReformController extends BaseController
{
    @Autowired
    private IInspectionLegislationReformService inspectionLegislationReformService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询立行立改 列表
     * @param  inspectionLegislationReform
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException{
        startPage();
        List<InspectionLegislationReformPO> data = inspectionLegislationReformService.selectInspectionLegislationReformList(inspectionLegislationReform);
        return getDataTable(data);
    }

    /**
     * 查询立行立改 文件
     * @param  inspectionLegislationReformId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/file")
    public AjaxResult findInspectionLegislationReformFile(@RequestParam("legislationReformId") String inspectionLegislationReformId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("LegislationReform" + inspectionLegislationReformId));
    }

    /**
     * 查询立行立改 总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionLegislationReformList(String planId) throws BaseException{
        return AjaxResult.success(inspectionLegislationReformService.selectInspectionLegislationReformCount(planId));
    }
    /**
     * 新增立行立改 
     * @param  inspectionLegislationReform
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "立行立改 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionLegislationReformPO inspectionLegislationReform) {
        if (null == inspectionLegislationReform) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionLegislationReform.getIsApproval()) {
            inspectionLegislationReform.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionLegislationReform.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionLegislationReformPO.class, inspectionLegislationReform);
        return toAjax(inspectionLegislationReformService.save(inspectionLegislationReform));
    }

    /**
     * 审批立行立改
     * @param  inspectionLegislationReformList
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "立行立改 ", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionLegislationReformPO> inspectionLegislationReformList) {
        if (CollectionUtils.isEmpty(inspectionLegislationReformList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionLegislationReformService.approvals(inspectionLegislationReformList);
        return AjaxResult.success();
    }

    /**
     * 修改立行立改 
     * @param  inspectionLegislationReform
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "立行立改 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionLegislationReformPO inspectionLegislationReform) throws BaseException{
        if (null == inspectionLegislationReform) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionLegislationReform.getIsApproval()) {
            inspectionLegislationReform.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionLegislationReform.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionLegislationReformPO.class, inspectionLegislationReform);
        return toAjax(inspectionLegislationReformService.update(inspectionLegislationReform));
    }

    /**
     * 删除立行立改 
     * legislationReformIds 立行立改 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "立行立改 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{legislationReformIds}")
    public AjaxResult remove(@PathVariable Long[] legislationReformIds) throws BaseException{
        return toAjax(inspectionLegislationReformService.deleteByInspectionLegislationReformIds(legislationReformIds));
    }
}
