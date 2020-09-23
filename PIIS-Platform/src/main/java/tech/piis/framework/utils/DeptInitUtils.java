package tech.piis.framework.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.mapper.SysDeptMapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.framework.utils
 * User: Tuzki
 * Date: 2020/9/15
 * Time: 20:14
 * Description:部门数据初始化
 */
@Component
public class DeptInitUtils {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public void initDept(List<SysDept> list) {
        //遍历全部，父ID查询节点
        for (SysDept sysDept : list) {
            String parentId = sysDept.getParentId();
            if ("-1".equals(parentId)) {
                continue;
            }
            sysDept.setAncestors(buildAncestors(parentId));
            sysDept.setLeaf(sysDeptMapper.hasChildByDeptId(sysDept.getDeptId()) <= 0);
            sysDeptMapper.updateDept(sysDept);
        }
    }

    /**
     * 构建祖先字段
     *
     * @param parentId
     * @return
     */
    public String buildAncestors(String parentId) {
        StringBuilder ancestors = new StringBuilder();
        while (!"-1".equals(parentId)) {
            SysDept parentDept = sysDeptMapper.selectDeptByParentId(parentId);
            if(null == parentDept){
                break;
            }
            ancestors.append(parentDept.getDeptId()).append(",");
            parentId = parentDept.getParentId();
        }
        return ancestors.toString().substring(0,ancestors.toString().length()-1);
    }


}
