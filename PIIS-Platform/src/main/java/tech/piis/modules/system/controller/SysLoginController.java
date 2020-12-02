package tech.piis.modules.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.constant.Constants;
import tech.piis.common.utils.ServletUtils;
import tech.piis.framework.security.LoginBody;
import tech.piis.framework.security.LoginUser;
import tech.piis.framework.security.service.SysLoginService;
import tech.piis.framework.security.service.SysPermissionService;
import tech.piis.framework.security.service.TokenService;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.system.domain.SysMenu;
import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.domain.vo.RouterVo;
import tech.piis.modules.system.domain.vo.RouterWithButtonVO;
import tech.piis.modules.system.mapper.SysUserDetailMapper;
import tech.piis.modules.system.service.ISysMenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserDetailMapper userDetailMapper;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha  验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息V2
     *
     * @return
     */
    @GetMapping("user/info")
    public AjaxResult getUserWithResource() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        if (null != user) {
            List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
            menuService.buildMenus(menus);
            AjaxResult ajaxResult = AjaxResult.success();
            ajaxResult.put("user", userDetailMapper.selectUserAllByUserId(user.getUserId()));
            ajaxResult.put("router", menus);
            return ajaxResult;
        }
        return AjaxResult.error();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    @Deprecated
    public AjaxResult getInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * *
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuType("F");
        List<RouterVo> router = menuService.buildMenus(menus);
        List<SysMenu> menuList = menuService.selectMenuList(sysMenu, user.getUserId());
        RouterWithButtonVO routerWithButtonVO = new RouterWithButtonVO();
        List<String> buttonList;
        if (!CollectionUtils.isEmpty(menuList)) {
            buttonList = new ArrayList<>(menuList.size());
            for (SysMenu menu : menuList) {
                String perms = menu.getPerms();
                if (!StringUtils.isEmpty(perms)) {
                    buttonList.add(perms);
                }
            }
            String[] buttonArr = new String[buttonList.size()];
            routerWithButtonVO.setButton(buttonList.toArray(buttonArr));
        }
        routerWithButtonVO.setRouter(router);
        return AjaxResult.success(routerWithButtonVO);
    }
}
