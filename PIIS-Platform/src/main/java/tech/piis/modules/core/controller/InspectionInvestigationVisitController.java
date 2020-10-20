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
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPO;
import tech.piis.modules.core.service.IInspectionInvestigationVisitService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 调研走访Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/investigation/visit")
public class InspectionInvestigationVisitController extends BaseController
{
    @Autowired
    private IInspectionInvestigationVisitService inspectionInvestigationVisitService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询调研走访列表
     * @param  inspectionInvestigationVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException{
        startPage();
        List<InspectionInvestigationVisitPO> data = inspectionInvestigationVisitService.selectInspectionInvestigationVisitList(inspectionInvestigationVisit);
        return getDataTable(data);
    }

    /**
     * 查询调研走访文件
     * @param  inspectionInvestigationVisitId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionInvestigationVisitFile(String inspectionInvestigationVisitId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionInvestigationVisitId));
    }

    /**
     * 查询调研走访总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionInvestigationVisitList(String planId) throws BaseException{
        return AjaxResult.success(inspectionInvestigationVisitService.selectInspectionInvestigationVisitCount(planId));
    }
    /**
     * 新增调研走访
     * @param  inspectionInvestigationVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:add')")
    @Log(title = "调研走访", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionInvestigationVisitPO inspectionInvestigationVisit) {
        if (null == inspectionInvestigationVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionInvestigationVisitService.save(inspectionInvestigationVisit));
    }

    /**
     * 修改调研走访
     * @param  inspectionInvestigationVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit:edit')")
    @Log(title = "调研走访", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException{
        if (null == inspectionInvestigationVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
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
    public AjaxResult remove(@PathVariable String[] investigationVisitIds) throws BaseException{
        return toAjax(inspectionInvestigationVisitService.deleteByInspectionInvestigationVisitIds(investigationVisitIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionInvestigationVisit
    */
    private void specialReportCovert2String(InspectionInvestigationVisitPO inspectionInvestigationVisit){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionInvestigationVisitList
    */
    private void specialReportCovert2List(List<InspectionInvestigationVisitPO> inspectionInvestigationVisitList){

    }
}
