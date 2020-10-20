package tech.piis.modules.core.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingDetailPO;
import tech.piis.modules.core.service.IInspectionSinkingUnderstandingDetailService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 下沉了解详情Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/sinking/understanding/detail")
public class InspectionSinkingUnderstandingDetailController extends BaseController
{
    @Autowired
    private IInspectionSinkingUnderstandingDetailService inspectionSinkingUnderstandingDetailService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询下沉了解详情列表
     * @param  inspectionSinkingUnderstandingDetail
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding/detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException{
        startPage();
        List<InspectionSinkingUnderstandingDetailPO> data = inspectionSinkingUnderstandingDetailService.selectInspectionSinkingUnderstandingDetailList(inspectionSinkingUnderstandingDetail);
        return getDataTable(data);
    }

    /**
     * 查询下沉了解详情文件
     * @param  inspectionSinkingUnderstandingDetailId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding/detail:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionSinkingUnderstandingDetailFile(String inspectionSinkingUnderstandingDetailId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionSinkingUnderstandingDetailId));
    }

    /**
     * 查询下沉了解详情总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding/detail:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionSinkingUnderstandingDetailList(String planId) throws BaseException{
        return AjaxResult.success(inspectionSinkingUnderstandingDetailService.selectInspectionSinkingUnderstandingDetailCount(planId));
    }
    /**
     * 新增下沉了解详情
     * @param  inspectionSinkingUnderstandingDetail
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding/detail:add')")
    @Log(title = "下沉了解详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) {
        if (null == inspectionSinkingUnderstandingDetail) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionSinkingUnderstandingDetailService.save(inspectionSinkingUnderstandingDetail));
    }

    /**
     * 修改下沉了解详情
     * @param  inspectionSinkingUnderstandingDetail
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding/detail:edit')")
    @Log(title = "下沉了解详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException{
        if (null == inspectionSinkingUnderstandingDetail) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionSinkingUnderstandingDetailPO.class, inspectionSinkingUnderstandingDetail);
        return toAjax(inspectionSinkingUnderstandingDetailService.update(inspectionSinkingUnderstandingDetail));
    }

    /**
     * 删除下沉了解详情
     * sinkngUnderstandingDetailIds 下沉了解详情ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sinking/understanding/detail:remove')")
    @Log(title = "下沉了解详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sinkngUnderstandingDetailIds}")
    public AjaxResult remove(@PathVariable Long[] sinkngUnderstandingDetailIds) throws BaseException{
        return toAjax(inspectionSinkingUnderstandingDetailService.deleteByInspectionSinkingUnderstandingDetailIds(sinkngUnderstandingDetailIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionSinkingUnderstandingDetail
    */
    private void specialReportCovert2String(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionSinkingUnderstandingDetailList
    */
    private void specialReportCovert2List(List<InspectionSinkingUnderstandingDetailPO> inspectionSinkingUnderstandingDetailList){

    }
}
