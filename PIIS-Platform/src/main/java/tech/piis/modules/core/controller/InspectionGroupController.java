package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionGroupPO;
import tech.piis.modules.core.service.IInspectionGroupService;

/**
 * 巡视组信息 Controller
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/system/group")
public class InspectionGroupController extends BaseController {
    @Autowired
    private IInspectionGroupService inspectionGroupService;

    /**
     * 查询巡视组信息 列表
     */
    @PreAuthorize("@ss.hasPermi('system:group:list')")
    @GetMapping("/list")
    public AjaxResult list(InspectionGroupPO inspectionGroupPO) {
        return null;
    }


    /**
     * 新增巡视组信息
     */
    @PreAuthorize("@ss.hasPermi('system:group:add')")
    @Log(title = "巡视组信息 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionGroupPO inspectionGroupPO) {
        return toAjax(0);
    }

    /**
     * 修改巡视组信息
     */
    @PreAuthorize("@ss.hasPermi('system:group:edit')")
    @Log(title = "巡视组信息 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionGroupPO inspectionGroupPO) {
        return toAjax(0);
    }

    /**
     * 删除巡视组信息
     */
    @PreAuthorize("@ss.hasPermi('system:group:remove')")
    @Log(title = "巡视组信息 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(0);
    }
}
