package tech.piis.modules.system.controller;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.ResultEnum;
import tech.piis.common.constant.UserConstants;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.DeptInitUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.service.ISysDeptService;

import java.util.List;

/**
 * 部门信息
 *
 * @author Kevin<EastascendWang                                                                                                                               @                                                                                                                               gmail.com>
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private DeptInitUtils deptInitUtils;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDept dept) {
        if (null != dept) {
            Integer pageNum = dept.getPageNum() * dept.getPageSize();
            dept.setPageNum(pageNum);
        }

        List<SysDept> depts = deptService.selectDeptList(dept);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(depts)
                .setTotal(deptService.count(dept));
    }

    /**
     * 根据部门ID查询树结构
     * <p>
     * unKnown
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping("tree/{deptId}")
    public AjaxResult getDeptTreeById(@PathVariable String deptId) {
        return AjaxResult.success(this.deptService.selectDeptTreeById(deptId));
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable String deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     * <p>
     * unKnown
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     * <p>
     * unKnown
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 根据部门ID查询下级部门信息
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/find/{deptId}")
    public AjaxResult findDeptById(@PathVariable("deptId") String deptId) {
        List<SysDept> sysDepts = this.deptService.selectChildrenDeptByParentId(deptId);
        return AjaxResult.success(sysDepts);
    }

    /**
     * 查询根部门节点
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "parentNode")
    public AjaxResult findParentNode() {
        return AjaxResult.success(this.deptService.selectDeptById("0000000000"));
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return AjaxResult.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDept dept) {
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable String deptId) {
        if (!deptService.hasChildByDeptId(deptId)) {
            return AjaxResult.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.error("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 初始化部门数据
     *
     * @return
     */
    @PostMapping("init")
    public AjaxResult initDept() {
        deptInitUtils.initDept(deptService.selectDeptList(null));
        return AjaxResult.success();
    }
}
