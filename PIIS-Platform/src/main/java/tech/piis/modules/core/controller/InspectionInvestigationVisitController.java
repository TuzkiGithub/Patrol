package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.GenConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPO;
import tech.piis.modules.core.service.IInspectionInvestigationVisitService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 调研走访Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/investigation/visit")
public class InspectionInvestigationVisitController extends BaseController {
    @Autowired
    private IInspectionInvestigationVisitService inspectionInvestigationVisitService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询调研走访列表
     *
     * @param inspectionInvestigationVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException {
        if (null != inspectionInvestigationVisit) {
            if (null == inspectionInvestigationVisit.getPageNum() || null == inspectionInvestigationVisit.getPageNum()) {
                inspectionInvestigationVisit.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionInvestigationVisit.setPageSize(GenConstants.DEFAULT_PAGE_SIZE);
            }
            inspectionInvestigationVisit.setPageNum(inspectionInvestigationVisit.getPageNum() * inspectionInvestigationVisit.getPageSize());
        }
        List<InspectionInvestigationVisitPO> data = inspectionInvestigationVisitService.selectInspectionInvestigationVisitList(inspectionInvestigationVisit);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(inspectionInvestigationVisitService.count(inspectionInvestigationVisit));
    }

    /**
     * 查询调研走访文件
     *
     * @param investigationVisitId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionInvestigationVisitFile(String investigationVisitId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("InvestigationVisit" + investigationVisitId));
    }

    /**
     * 查询调研走访总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionInvestigationVisitList(String planId) throws BaseException {
        return AjaxResult.success(inspectionInvestigationVisitService.selectInspectionInvestigationVisitCount(planId));
    }

    /**
     * 新增调研走访
     *
     * @param inspectionInvestigationVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:add')")
    @Log(title = "调研走访", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionInvestigationVisitPO inspectionInvestigationVisit) {
        if (null == inspectionInvestigationVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionInvestigationVisit.getIsApproval()) {
            inspectionInvestigationVisit.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionInvestigationVisit.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionInvestigationVisitPO.class, inspectionInvestigationVisit);
        return toAjax(inspectionInvestigationVisitService.save(inspectionInvestigationVisit));
    }

    /**
     * 审批调研走访
     *
     * @param inspectionInvestigationVisitList
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:approval')")
    @Log(title = "调研走访", businessType = BusinessType.INSERT)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionInvestigationVisitPO> inspectionInvestigationVisitList) {
        if (CollectionUtils.isEmpty(inspectionInvestigationVisitList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionInvestigationVisitService.doApprovals(inspectionInvestigationVisitList);
        return AjaxResult.success();
    }

    /**
     * 修改调研走访
     *
     * @param inspectionInvestigationVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:edit')")
    @Log(title = "调研走访", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException {
        if (null == inspectionInvestigationVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionInvestigationVisit.getIsApproval()) {
            inspectionInvestigationVisit.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionInvestigationVisit.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionInvestigationVisitPO.class, inspectionInvestigationVisit);
        return toAjax(inspectionInvestigationVisitService.update(inspectionInvestigationVisit));
    }

    /**
     * 删除调研走访
     * investigationVisitIds 调研走访ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:remove')")
    @Log(title = "调研走访", businessType = BusinessType.DELETE)
    @DeleteMapping("/{investigationVisitIds}")
    public AjaxResult remove(@PathVariable String[] investigationVisitIds) throws BaseException {
        return toAjax(inspectionInvestigationVisitService.deleteByInspectionInvestigationVisitIds(investigationVisitIds));
    }
}
