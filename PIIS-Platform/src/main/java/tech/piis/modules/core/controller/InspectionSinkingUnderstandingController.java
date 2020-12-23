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
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingPO;
import tech.piis.modules.core.service.IInspectionSinkingUnderstandingService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 下沉了解Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/sinking/understanding")
public class InspectionSinkingUnderstandingController extends BaseController {
    @Autowired
    private IInspectionSinkingUnderstandingService inspectionSinkingUnderstandingService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询下沉了解列表
     *
     * @param inspectionSinkingUnderstanding
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        if (null != inspectionSinkingUnderstanding) {
            if (null == inspectionSinkingUnderstanding.getPageNum() || null == inspectionSinkingUnderstanding.getPageNum()) {
                inspectionSinkingUnderstanding.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionSinkingUnderstanding.setPageSize(GenConstants.DEFAULT_PAGE_SIZE);
            }
            inspectionSinkingUnderstanding.setPageNum(inspectionSinkingUnderstanding.getPageNum() * inspectionSinkingUnderstanding.getPageSize());
        }
        List<InspectionSinkingUnderstandingPO> data = inspectionSinkingUnderstandingService.selectInspectionSinkingUnderstandingList(inspectionSinkingUnderstanding);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(inspectionSinkingUnderstandingService.count(inspectionSinkingUnderstanding));
    }

    /**
     * 查询下沉了解文件
     *
     * @param sinkingUnderstandingId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/file")
    public AjaxResult findInspectionSinkingUnderstandingFile(String sinkingUnderstandingId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("SinkingUnderstanding" + sinkingUnderstandingId));
    }

    /**
     * 查询下沉了解总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionSinkingUnderstandingList(String planId) throws BaseException {
        return AjaxResult.success(inspectionSinkingUnderstandingService.selectInspectionSinkingUnderstandingCount(planId));
    }

    /**
     * 新增下沉了解
     *
     * @param inspectionSinkingUnderstanding
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "下沉了解", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) {
        if (null == inspectionSinkingUnderstanding) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionSinkingUnderstanding.getIsApproval()) {
            inspectionSinkingUnderstanding.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionSinkingUnderstanding.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionSinkingUnderstandingPO.class, inspectionSinkingUnderstanding);
        return toAjax(inspectionSinkingUnderstandingService.save(inspectionSinkingUnderstanding));
    }

    /**
     * 审批下沉了解
     *
     * @param sinkingUnderstandingList
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "下沉了解", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionSinkingUnderstandingPO> sinkingUnderstandingList) {
        if (CollectionUtils.isEmpty(sinkingUnderstandingList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionSinkingUnderstandingService.doApprovals(sinkingUnderstandingList);
        return AjaxResult.success();
    }

    /**
     * 修改下沉了解
     *
     * @param inspectionSinkingUnderstanding
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "下沉了解", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        if (null == inspectionSinkingUnderstanding) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionSinkingUnderstanding.getIsApproval()) {
            inspectionSinkingUnderstanding.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionSinkingUnderstanding.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionSinkingUnderstandingPO.class, inspectionSinkingUnderstanding);
        return toAjax(inspectionSinkingUnderstandingService.update(inspectionSinkingUnderstanding));
    }

    /**
     * 删除下沉了解
     * sinkngUnderstandingIds 下沉了解ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "下沉了解", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sinkngUnderstandingIds}")
    public AjaxResult remove(@PathVariable String[] sinkngUnderstandingIds) throws BaseException {
        return toAjax(inspectionSinkingUnderstandingService.deleteByInspectionSinkingUnderstandingIds(sinkngUnderstandingIds));
    }

}
