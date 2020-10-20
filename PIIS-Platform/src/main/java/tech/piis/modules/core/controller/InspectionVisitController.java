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
import tech.piis.modules.core.domain.po.InspectionVisitPO;
import tech.piis.modules.core.service.IInspectionVisitService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 来访Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/visit")
public class InspectionVisitController extends BaseController
{
    @Autowired
    private IInspectionVisitService inspectionVisitService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询来访列表
     * @param  inspectionVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionVisitPO inspectionVisit) throws BaseException{
        startPage();
        List<InspectionVisitPO> data = inspectionVisitService.selectInspectionVisitList(inspectionVisit);
        return getDataTable(data);
    }

    /**
     * 查询来访文件
     * @param  inspectionVisitId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:visit:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionVisitFile(String inspectionVisitId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionVisitId));
    }

    /**
     * 查询来访总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:visit:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionVisitList(String planId) throws BaseException{
        return AjaxResult.success(inspectionVisitService.selectInspectionVisitCount(planId));
    }
    /**
     * 新增来访
     * @param  inspectionVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:visit:add')")
    @Log(title = "来访", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionVisitPO inspectionVisit) {
        if (null == inspectionVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionVisitService.save(inspectionVisit));
    }

    /**
     * 修改来访
     * @param  inspectionVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:visit:edit')")
    @Log(title = "来访", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionVisitPO inspectionVisit) throws BaseException{
        if (null == inspectionVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionVisitPO.class, inspectionVisit);
        return toAjax(inspectionVisitService.update(inspectionVisit));
    }

    /**
     * 删除来访
     * callVisitIds 来访ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:visit:remove')")
    @Log(title = "来访", businessType = BusinessType.DELETE)
	@DeleteMapping("/{callVisitIds}")
    public AjaxResult remove(@PathVariable String[] callVisitIds) throws BaseException{
        return toAjax(inspectionVisitService.deleteByInspectionVisitIds(callVisitIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionVisit
    */
    private void specialReportCovert2String(InspectionVisitPO inspectionVisit){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionVisitList
    */
    private void specialReportCovert2List(List<InspectionVisitPO> inspectionVisitList){

    }
}
