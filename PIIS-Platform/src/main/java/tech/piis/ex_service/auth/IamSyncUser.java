package tech.piis.ex_service.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.modules.common.domain.IamResponse;
import tech.piis.modules.common.domain.IamUser;
import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.domain.SysUserDept;
import tech.piis.modules.system.domain.SysUserRole;
import tech.piis.modules.system.mapper.SysUserDeptMapper;
import tech.piis.modules.system.mapper.SysUserMapper;
import tech.piis.modules.system.mapper.SysUserRoleMapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.ex_service.auth
 * User: Tuzki
 * Date: 2020/10/29
 * Time: 10:23
 * Description:IAM 同步用户（为了兼容JT）
 */
@Slf4j
@RestController
@Transactional
public class IamSyncUser {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserDeptMapper userDeptMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Value("${piis.defaultRoleId}")
    private Long defaultRoleId;

    @PostMapping("/jtsi/IAMService/syncCreateUser")
    public IamResponse syncUser(@RequestBody List<IamUser> userList) {
        log.info("======IamSyncUser===== userList = {}", userList);
        try {
            if (!CollectionUtils.isEmpty(userList)) {
                userList.forEach(user -> {
                    SysUser sysUser = new SysUser();
                    sysUser.setUserId(user.getUserId());
                    sysUser.setPassword(SecurityUtils.encryptPassword(user.getUserId()));
                    sysUser.setPhonenumber(user.getMobile());
                    sysUser.setEmail(user.getEmail());
                    sysUser.setStatus("0");
                    sysUser.setDelFlag("0");
                    BizUtils.setCreatedOperation(SysUser.class, sysUser);

                    SysUserDept userDept = new SysUserDept();
                    userDept.setUserId(user.getUserId());
                    userDept.setDeptId(user.getOrgCode());
                    QueryWrapper<SysUserDept> userDeptQueryWrapper = new QueryWrapper<>();
                    List<SysUserDept> userDeptList = userDeptMapper.selectList(userDeptQueryWrapper);
                    //新增用户-部门关系
                    if (CollectionUtils.isEmpty(userDeptList)) {
                        userDeptMapper.insert(userDept);
                    }
                    SysUser currentUser = userMapper.selectUserById(user.getUserId());
                    if (null == currentUser) {
                        //新增用户
                        userMapper.insertUser(sysUser);
                        //绑定默认角色
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(user.getUserId());
                        userRole.setRoleId(defaultRoleId);
                        userRoleMapper.insert(userRole);
                    }

                });
            }
        } catch (Exception e) {
            log.error("======IamSyncUser===== e = {}", e);
            return new IamResponse().setResult("-1").setReturnMessage("同步用户失败！");
        }
        return new IamResponse().setResult("0").setReturnMessage("success");
    }
}
