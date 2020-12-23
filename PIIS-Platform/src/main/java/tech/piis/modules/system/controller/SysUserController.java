package tech.piis.modules.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.UserConstants;
import tech.piis.common.enums.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.ServletUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.security.LoginUser;
import tech.piis.framework.security.service.TokenService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.utils.poi.ExcelUtil;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.domain.vo.UserVO;
import tech.piis.modules.system.service.ISysPostService;
import tech.piis.modules.system.service.ISysRoleService;
import tech.piis.modules.system.service.ISysUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户信息
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        int count = userService.selectCount(user);
        List<UserVO> list = userService.selectUserList(user);
        //假分页
        Integer pageNum = user.getPageNum() * user.getPageSize();
        Integer pageSize = pageNum + user.getPageSize();
        if (!CollectionUtils.isEmpty(list)) {
            if (list.size() != 1) {
                if (pageSize > list.size()) {
                    pageSize = list.size();
                }
                if (pageNum <= pageSize) {
                    list = list.subList(pageNum, pageSize);
                } else {
                    list = new ArrayList<>();
                }
            }
        }
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(list)
                .setTotal(count);
    }

    /**
     * 根据用户名称模糊查询用户、部门、岗位信息
     * *
     *
     * @param user
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping("find")
    public AjaxResult findUserDeptPostInfo(SysUser user) {
        if (null == user) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return AjaxResult.success(userService.selectUserDeptPost(user));
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId") String userId) throws BaseException {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("roles", roleService.selectRoleAll());
        ajax.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            ajax.put(AjaxResult.DATA_TAG, userService.selectUserById(userId));
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserIdUnique(user.getUserId()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，用户账号已存在");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        SysUser currentUser = userService.selectUserById(user.getUserId());
        String oldPassword = currentUser.getPassword();
        String newPassword = user.getPassword();
        if (!Objects.equals(user.getPhonenumber(), currentUser.getPhonenumber())) {
            if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
                return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
            }
        }

        if (!Objects.equals(user.getEmail(), currentUser.getEmail())) {
            if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
                return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
            }
        }

        if (null != newPassword) {
            if (!Objects.equals(oldPassword, newPassword)) {
                user.setPassword(SecurityUtils.encryptPassword(newPassword));
            }
        }
        BizUtils.setUpdatedOperation(SysUser.class, user);
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable String[] userIds) {
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }
}