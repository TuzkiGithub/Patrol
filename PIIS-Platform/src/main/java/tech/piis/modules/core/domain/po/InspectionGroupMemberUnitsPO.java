package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.po
 * User: Tuzki
 * Date: 2020/10/12
 * Time: 17:01
 * Description:巡视组组员-被巡视单位关系实体
 */

@TableName("inspection_group_member_units")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionGroupMemberUnitsPO {
    @TableId(value = "GROUP_MEMBER_UNITS_ID", type = IdType.AUTO)
    private Long groupMemberUnitsId;

    @NotNull(message = "巡视组组员ID不能为空！")
    private Long groupMemberId;

    @NotNull(message = "巡被巡视单位ID不能为空！")
    private Long unitsId;

    private String groupId;
}
