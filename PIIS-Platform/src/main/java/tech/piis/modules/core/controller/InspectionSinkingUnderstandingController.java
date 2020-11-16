package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.GenConstants;
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
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        Long unitsId = null;
        if (null != inspectionSinkingUnderstanding) {
            unitsId = Long.parseLong(inspectionSinkingUnderstanding.getUnitsId());
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
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionSinkingUnderstandingFile(String sinkingUnderstandingId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("SinkingUnderstanding" + sinkingUnderstandingId));
    }

    /**
     * 查询下沉了解总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionSinkingUnderstandingList(String planId) throws BaseException {
        return AjaxResult.success(inspectionSinkingUnderstandingService.selectInspectionSinkingUnderstandingCount(planId));
    }

    /**
     * 新增下沉了解
     *
     * @param inspectionSinkingUnderstanding
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding:add')")
    @Log(title = "下沉了解", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) {
        if (null == inspectionSinkingUnderstanding) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionSinkingUnderstandingPO.class, inspectionSinkingUnderstanding);
        return toAjax(inspectionSinkingUnderstandingService.save(inspectionSinkingUnderstanding));
    }

    /**
     * 修改下沉了解
     *
     * @param inspectionSinkingUnderstanding
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding:edit')")
    @Log(title = "下沉了解", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        if (null == inspectionSinkingUnderstanding) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionSinkingUnderstandingPO.class, inspectionSinkingUnderstanding);
        return toAjax(inspectionSinkingUnderstandingService.update(inspectionSinkingUnderstanding));
    }

    /**
     * 删除下沉了解
     * sinkngUnderstandingIds 下沉了解ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding:remove')")
    @Log(title = "下沉了解", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sinkngUnderstandingIds}")
    public AjaxResult remove(@PathVariable String[] sinkngUnderstandingIds) throws BaseException {
        return toAjax(inspectionSinkingUnderstandingService.deleteByInspectionSinkingUnderstandingIds(sinkngUnderstandingIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionSinkingUnderstanding
     */
    private void specialReportCovert2String(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) {

    }


    /**
     * 参数类型转换
     *
     * @param inspectionSinkingUnderstandingList
     */
    private void specialReportCovert2List(List<InspectionSinkingUnderstandingPO> inspectionSinkingUnderstandingList) {

    }
}
