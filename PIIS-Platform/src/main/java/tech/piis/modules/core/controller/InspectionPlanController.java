package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.service.IInspectionPlanService;

/**
 * 巡视计划 Controller
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/system/plan")
public class InspectionPlanController extends BaseController {
    @Autowired
    private IInspectionPlanService inspectionPlanService;

    /**
     * 查询巡视计划 列表
     */
    @PreAuthorize("@ss.hasPermi('system:plan:list')")
    @GetMapping("/list")
    public AjaxResult list(InspectionPlanPO inspectionPlanPO) {
        return AjaxResult.success(inspectionPlanService.selectPlanList(inspectionPlanPO));
    }


    /**
     * 新增巡视计划
     */
    @PreAuthorize("@ss.hasPermi('system:plan:add')")
    @Log(title = "巡视计划 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionPlanPO inspectionPlanPO) {
        return toAjax(inspectionPlanService.savePlan(inspectionPlanPO));
    }

    /**
     * 修改巡视计划
     */
    @PreAuthorize("@ss.hasPermi('system:plan:edit')")
    @Log(title = "巡视计划 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionPlanPO inspectionPlanPO) {
        return toAjax(0);
    }

    /**
     * 删除巡视计划
     */
    @PreAuthorize("@ss.hasPermi('system:plan:remove')")
    @Log(title = "巡视计划 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(this.inspectionPlanService.delPlanByIds(ids));
    }
}
