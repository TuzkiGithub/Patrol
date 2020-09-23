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
import tech.piis.modules.core.domain.po.InspectionGroupMemberPO;
import tech.piis.modules.core.service.IInspectionGroupMemberService;

/**
 * 巡视组组员 Controller
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/system/member")
public class InspectionGroupMemberController extends BaseController
{
    @Autowired
    private IInspectionGroupMemberService inspectionGroupMemberService;

    /**
     * 查询巡视组组员 列表
     */
    @PreAuthorize("@ss.hasPermi('system:member:list')")
    @GetMapping("/list")
    public AjaxResult list(InspectionGroupMemberPO inspectionGroupMemberPO) {
        return null;
    }


    /**
     * 新增巡视组组员 
     */
    @PreAuthorize("@ss.hasPermi('system:member:add')")
    @Log(title = "巡视组组员 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionGroupMemberPO inspectionGroupMemberPO) {
        return toAjax(0);
    }

    /**
     * 修改巡视组组员 
     */
    @PreAuthorize("@ss.hasPermi('system:member:edit')")
    @Log(title = "巡视组组员 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionGroupMemberPO inspectionGroupMemberPO) {
        return toAjax(0);
    }

    /**
     * 删除巡视组组员 
     */
    @PreAuthorize("@ss.hasPermi('system:member:remove')")
    @Log(title = "巡视组组员 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(0);
    }
}
