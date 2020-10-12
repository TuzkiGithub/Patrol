package tech.piis.modules.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: com.ebchinatech.modules.sys.system.domain
 * User: Tuzki
 * Date: 2020/6/3
 * Time: 15:02
 * Description:用户部门关系表
 */
@TableName("sys_user_dept")
@Data
@Accessors(chain = true)
public class SysUserDept {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String userId;
    private String deptId;
}
