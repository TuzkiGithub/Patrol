package tech.piis.modules.system.mapper;

import tech.piis.modules.system.domain.SysUserPost;
import tech.piis.modules.system.domain.SysUserPostVO;

import java.util.List;

/**
 * 用户与岗位关联表 数据层
 * 
 * @author Kevin<EastascendWang@gmail.com>
 */
public interface SysUserPostMapper
{
    /**
     * 通过用户ID删除用户和岗位关联
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserPostByUserId(String userId);

    /**
     * 通过岗位ID查询岗位使用数量
     * 
     * @param postId 岗位ID
     * @return 结果
     */
    public int countUserPostById(String postId);

    /**
     * 批量删除用户和岗位关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserPost(Long[] ids);

    /**
     * 批量新增用户岗位信息
     * 
     * @param userPostList 用户角色列表
     * @return 结果
     */
    public int batchUserPost(List<SysUserPost> userPostList);

    /**
     * 新增用户-岗位
     * @param post
     * @return
     */
    public int saveUserPost(SysUserPost post);

    /**
     * 查询用户-岗位
     * @param post
     * @return
     */
    public SysUserPostVO selectUserPost(SysUserPostVO post);

    /**
     * 根据用户ID批量删除用户-岗位关系
     * @param userPosts
     * @return
     */
    public int delUserPostBatch(List<SysUserPost> userPosts);

}
