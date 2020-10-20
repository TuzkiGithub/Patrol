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
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPersonPO;
import tech.piis.modules.core.service.IInspectionInvestigationVisitPersonService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 调研走访人员Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/investigation/visit/person")
public class InspectionInvestigationVisitPersonController extends BaseController
{
    @Autowired
    private IInspectionInvestigationVisitPersonService inspectionInvestigationVisitPersonService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询调研走访人员列表
     * @param  inspectionInvestigationVisitPerson
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit/person:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException{
        startPage();
        List<InspectionInvestigationVisitPersonPO> data = inspectionInvestigationVisitPersonService.selectInspectionInvestigationVisitPersonList(inspectionInvestigationVisitPerson);
        return getDataTable(data);
    }

    /**
     * 查询调研走访人员文件
     * @param  inspectionInvestigationVisitPersonId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit/person:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionInvestigationVisitPersonFile(String inspectionInvestigationVisitPersonId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionInvestigationVisitPersonId));
    }

    /**
     * 查询调研走访人员总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit/person:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionInvestigationVisitPersonList(String planId) throws BaseException{
        return AjaxResult.success(inspectionInvestigationVisitPersonService.selectInspectionInvestigationVisitPersonCount(planId));
    }
    /**
     * 新增调研走访人员
     * @param  inspectionInvestigationVisitPerson
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit/person:add')")
    @Log(title = "调研走访人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) {
        if (null == inspectionInvestigationVisitPerson) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionInvestigationVisitPersonService.save(inspectionInvestigationVisitPerson));
    }

    /**
     * 修改调研走访人员
     * @param  inspectionInvestigationVisitPerson
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit/person:edit')")
    @Log(title = "调研走访人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException{
        if (null == inspectionInvestigationVisitPerson) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionInvestigationVisitPersonPO.class, inspectionInvestigationVisitPerson);
        return toAjax(inspectionInvestigationVisitPersonService.update(inspectionInvestigationVisitPerson));
    }

    /**
     * 删除调研走访人员
     * investigationVisitPersonIds 调研走访人员ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:investigation/visit/person:remove')")
    @Log(title = "调研走访人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{investigationVisitPersonIds}")
    public AjaxResult remove(@PathVariable Long[] investigationVisitPersonIds) throws BaseException{
        return toAjax(inspectionInvestigationVisitPersonService.deleteByInspectionInvestigationVisitPersonIds(investigationVisitPersonIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionInvestigationVisitPerson
    */
    private void specialReportCovert2String(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionInvestigationVisitPersonList
    */
    private void specialReportCovert2List(List<InspectionInvestigationVisitPersonPO> inspectionInvestigationVisitPersonList){

    }
}
