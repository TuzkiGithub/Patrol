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
import tech.piis.modules.core.domain.po.InspectionConsultInfoDetailPO;
import tech.piis.modules.core.service.IInspectionConsultInfoDetailService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 查阅资料详情Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/consult/info/detail")
public class InspectionConsultInfoDetailController extends BaseController
{
    @Autowired
    private IInspectionConsultInfoDetailService inspectionConsultInfoDetailService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询查阅资料详情列表
     * @param  inspectionConsultInfoDetail
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info/detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException{
        startPage();
        List<InspectionConsultInfoDetailPO> data = inspectionConsultInfoDetailService.selectInspectionConsultInfoDetailList(inspectionConsultInfoDetail);
        return getDataTable(data);
    }

    /**
     * 查询查阅资料详情文件
     * @param  inspectionConsultInfoDetailId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info/detail:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionConsultInfoDetailFile(String inspectionConsultInfoDetailId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionConsultInfoDetailId));
    }

    /**
     * 查询查阅资料详情总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:consult/info/detail:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionConsultInfoDetailList(String planId) throws BaseException{
        return AjaxResult.success(inspectionConsultInfoDetailService.selectInspectionConsultInfoDetailCount(planId));
    }
    /**
     * 新增查阅资料详情
     * @param  inspectionConsultInfoDetail
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info/detail:add')")
    @Log(title = "查阅资料详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionConsultInfoDetailPO inspectionConsultInfoDetail) {
        if (null == inspectionConsultInfoDetail) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionConsultInfoDetailService.save(inspectionConsultInfoDetail));
    }

    /**
     * 修改查阅资料详情
     * @param  inspectionConsultInfoDetail
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info/detail:edit')")
    @Log(title = "查阅资料详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException{
        if (null == inspectionConsultInfoDetail) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionConsultInfoDetailPO.class, inspectionConsultInfoDetail);
        return toAjax(inspectionConsultInfoDetailService.update(inspectionConsultInfoDetail));
    }

    /**
     * 删除查阅资料详情
     * consultInfoDetailIds 查阅资料详情ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info/detail:remove')")
    @Log(title = "查阅资料详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{consultInfoDetailIds}")
    public AjaxResult remove(@PathVariable Long[] consultInfoDetailIds) throws BaseException{
        return toAjax(inspectionConsultInfoDetailService.deleteByInspectionConsultInfoDetailIds(consultInfoDetailIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionConsultInfoDetail
    */
    private void specialReportCovert2String(InspectionConsultInfoDetailPO inspectionConsultInfoDetail){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionConsultInfoDetailList
    */
    private void specialReportCovert2List(List<InspectionConsultInfoDetailPO> inspectionConsultInfoDetailList){

    }
}
