package tech.piis.modules.core.controller;

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
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionUnitsPO;
import tech.piis.modules.core.service.IInspectionUnitsService;

/**
 * 被巡视单位 Controller
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/system/units")
public class InspectionUnitsController extends BaseController
{
    @Autowired
    private IInspectionUnitsService inspectionUnitsService;

    /**
     * 查询被巡视单位 列表
     */
    @PreAuthorize("@ss.hasPermi('system:units:list')")
    @GetMapping("/list")
    public AjaxResult list(InspectionUnitsPO inspectionUnitsPO) {
        return null;
    }


    /**
     * 新增被巡视单位 
     */
    @PreAuthorize("@ss.hasPermi('system:units:add')")
    @Log(title = "被巡视单位 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionUnitsPO inspectionUnitsPO) {
        return toAjax(0);
    }

    /**
     * 修改被巡视单位 
     */
    @PreAuthorize("@ss.hasPermi('system:units:edit')")
    @Log(title = "被巡视单位 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionUnitsPO inspectionUnitsPO) {
        return toAjax(0);
    }

    /**
     * 删除被巡视单位 
     */
    @PreAuthorize("@ss.hasPermi('system:units:remove')")
    @Log(title = "被巡视单位 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(0);
    }
}
