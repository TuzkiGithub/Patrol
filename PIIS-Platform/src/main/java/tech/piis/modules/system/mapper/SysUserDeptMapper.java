package tech.piis.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.system.domain.SysUserDept;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.system.mapper
 * User: Tuzki
 * Date: 2020/9/11
 * Time: 17:35
 * Description:
 */
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {

    /**
     * 批量插入
     *
     * @param userDepts
     * @return
     */
    public int batchUserDept(List<SysUserDept> userDepts);

    /**
     * 批量删除
     * @param userDepts
     * @return
     */
    public int delUserDeptBatch(List<SysUserDept> userDepts);
}
