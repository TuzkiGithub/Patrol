package tech.piis.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tech.piis.modules.system.domain.SysDept;

import java.util.List;

/**
 * 部门管理 数据层
 *
 * @author Kevin<EastascendWang   @   gmail.com>
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    public List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(String deptId);

    /**
     * 根据ID查询所有子部门
     *
     * @param deptId 部门ID
     * @return 部门列表
     */
    public List<SysDept> selectChildrenDeptById(String deptId);

    /**
     * 根据父ID查询其下部门
     *
     * @param deptId
     * @return
     */
    public List<SysDept> selectChildrenDeptByParentId(String deptId);

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int hasChildByDeptId(String deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int checkDeptExistUser(String deptId);

    /**
     * 校验部门名称是否唯一
     *
     * @param deptName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") String parentId);

    /**
     * 新增部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改部门信息
     *
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 修改所在部门的父级部门状态
     *
     * @param dept 部门
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * 修改子元素关系
     *
     * @param depts 子元素
     * @return 结果
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(String deptId);

    /**
     * 根据父ID批量查询
     *
     * @param deptIds
     * @return
     */
    public List<SysDept> selectDeptByParentIds(String[] deptIds);


    /**
     * 根据父ID查询部门
     *
     * @param parentId
     * @return
     */
    public SysDept selectDeptByParentId(String parentId);

    /**
     * 批量新增部门
     * @param sysDeptList
     * @return
     */
    int insertDeptBatch(List<SysDept> sysDeptList);

    /**
     * 根据ancestors查询上级部门
     * @param parentIds
     * @return
     */
    List<SysDept> selectDeptsById(String[] parentIds);
}
