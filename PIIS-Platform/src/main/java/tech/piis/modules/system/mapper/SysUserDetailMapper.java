package tech.piis.modules.system.mapper;

import tech.piis.modules.system.domain.SysUser;
import tech.piis.modules.system.domain.vo.UserVO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: com.ebchinatech.modules.sys.system.mapper
 * User: Tuzki
 * Date: 2020/6/4
 * Time: 11:14
 * Description:
 */
public interface SysUserDetailMapper {
    /**
     * 根据userId查询用户所有相关数据
     *
     * @param userId
     * @return
     */
    UserVO selectUserAllByUserId(String userId);

    /**
     * 根据userId查询用户、部门数据
     *
     * @param userId
     * @return
     */
    UserVO selectUserDeptByUserId(String userId);

    /**
     * 根据userId查询用户、部门、角色数据
     *
     * @param userId
     * @return
     */
    UserVO selectUserDeptRoleByUserId(String userId);

    /**
     * 根据dept查看用户
     *
     * @param deptId
     * @return
     */
    List<UserVO> selectUserDeptByDeptId(String deptId);

    /**
     * 查询用户-部门-角色
     * @return
     */
    List<UserVO> selectUserDeptRole(SysUser user);

    /**
     * 查询用户-部门-岗位
     * @param user
     * @return
     */
    List<UserVO> selectUserDeptPost(SysUser user);

    /**
     * 根据部门ID查询用户详情
     * @param deptId
     * @return
     */
    int selectCount(String deptId);
}
