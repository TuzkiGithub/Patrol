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
import tech.piis.modules.core.domain.po.InspectionCheckPersonMattersPO;
import tech.piis.modules.core.service.IInspectionCheckPersonMattersService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 抽查个人事项报告Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/check/matters")
public class InspectionCheckPersonMattersController extends BaseController
{
    @Autowired
    private IInspectionCheckPersonMattersService inspectionCheckPersonMattersService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询抽查个人事项报告列表
     * @param  inspectionCheckPersonMatters
     */
    @PreAuthorize("@ss.hasPermi('piis:check/matters:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException{
        startPage();
        List<InspectionCheckPersonMattersPO> data = inspectionCheckPersonMattersService.selectInspectionCheckPersonMattersList(inspectionCheckPersonMatters);
        return getDataTable(data);
    }

    /**
     * 查询抽查个人事项报告文件
     * @param  inspectionCheckPersonMattersId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:check/matters:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionCheckPersonMattersFile(String inspectionCheckPersonMattersId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionCheckPersonMattersId));
    }

    /**
     * 查询抽查个人事项报告总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:check/matters:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionCheckPersonMattersList(String planId) throws BaseException{
        return AjaxResult.success(inspectionCheckPersonMattersService.selectInspectionCheckPersonMattersCount(planId));
    }
    /**
     * 新增抽查个人事项报告
     * @param  inspectionCheckPersonMatters
     */
    @PreAuthorize("@ss.hasPermi('piis:check/matters:add')")
    @Log(title = "抽查个人事项报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionCheckPersonMattersPO inspectionCheckPersonMatters) {
        if (null == inspectionCheckPersonMatters) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionCheckPersonMattersService.save(inspectionCheckPersonMatters));
    }

    /**
     * 修改抽查个人事项报告
     * @param  inspectionCheckPersonMatters
     */
    @PreAuthorize("@ss.hasPermi('piis:check/matters:edit')")
    @Log(title = "抽查个人事项报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException{
        if (null == inspectionCheckPersonMatters) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionCheckPersonMattersPO.class, inspectionCheckPersonMatters);
        return toAjax(inspectionCheckPersonMattersService.update(inspectionCheckPersonMatters));
    }

    /**
     * 删除抽查个人事项报告
     * checkPersonMattersIds 抽查个人事项报告ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:check/matters:remove')")
    @Log(title = "抽查个人事项报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{checkPersonMattersIds}")
    public AjaxResult remove(@PathVariable Long[] checkPersonMattersIds) throws BaseException{
        return toAjax(inspectionCheckPersonMattersService.deleteByInspectionCheckPersonMattersIds(checkPersonMattersIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionCheckPersonMatters
    */
    private void specialReportCovert2String(InspectionCheckPersonMattersPO inspectionCheckPersonMatters){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionCheckPersonMattersList
    */
    private void specialReportCovert2List(List<InspectionCheckPersonMattersPO> inspectionCheckPersonMattersList){

    }
}
