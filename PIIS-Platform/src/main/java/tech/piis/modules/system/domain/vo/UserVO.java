package tech.piis.modules.system.domain.vo;


import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.framework.web.domain.BaseEntity;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.domain.SysRole;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: com.ebchinatech.modules.sys.system.domain.vo
 * User: Tuzki
 * Date: 2020/6/4
 * Time: 10:37
 * Description:用户VO
 */
@Data
@Accessors(chain = true)
public class UserVO extends BaseEntity {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 用户部门关系Id
     */
    private Long userDeptId;


    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登陆IP
     */
    private String loginIp;

    /**
     * 最后登陆时间
     */
    private Date loginDate;

    /**
     * 部门列表
     */
    private List<SysDept> depts;

    /**
     * 角色对象
     */
    private List<SysRole> roles;

    /**
     * 权限列表
     */
    private List<String> permissions;

    /**
     * 部门名称
     */
    private String deptStr;

    /**
     * 角色名称
     */
    private String roleStr;

}
