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
import tech.piis.modules.core.domain.po.InspectionCallVisitPO;
import tech.piis.modules.core.service.IInspectionCallVisitService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 来电Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/call/visit")
public class InspectionCallVisitController extends BaseController
{
    @Autowired
    private IInspectionCallVisitService inspectionCallVisitService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询来电列表
     * @param  inspectionCallVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionCallVisitPO inspectionCallVisit) throws BaseException{
        startPage();
        List<InspectionCallVisitPO> data = inspectionCallVisitService.selectInspectionCallVisitList(inspectionCallVisit);
        return getDataTable(data);
    }

    /**
     * 查询来电文件
     * @param  inspectionCallVisitId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionCallVisitFile(String inspectionCallVisitId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionCallVisitId));
    }

    /**
     * 查询来电总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionCallVisitList(String planId) throws BaseException{
        return AjaxResult.success(inspectionCallVisitService.selectInspectionCallVisitCount(planId));
    }
    /**
     * 新增来电
     * @param  inspectionCallVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:add')")
    @Log(title = "来电", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionCallVisitPO inspectionCallVisit) {
        if (null == inspectionCallVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionCallVisitService.save(inspectionCallVisit));
    }

    /**
     * 修改来电
     * @param  inspectionCallVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:edit')")
    @Log(title = "来电", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionCallVisitPO inspectionCallVisit) throws BaseException{
        if (null == inspectionCallVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionCallVisitPO.class, inspectionCallVisit);
        return toAjax(inspectionCallVisitService.update(inspectionCallVisit));
    }

    /**
     * 删除来电
     * callVisitIds 来电ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:remove')")
    @Log(title = "来电", businessType = BusinessType.DELETE)
	@DeleteMapping("/{callVisitIds}")
    public AjaxResult remove(@PathVariable String[] callVisitIds) throws BaseException{
        return toAjax(inspectionCallVisitService.deleteByInspectionCallVisitIds(callVisitIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionCallVisit
    */
    private void specialReportCovert2String(InspectionCallVisitPO inspectionCallVisit){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionCallVisitList
    */
    private void specialReportCovert2List(List<InspectionCallVisitPO> inspectionCallVisitList){

    }
}
