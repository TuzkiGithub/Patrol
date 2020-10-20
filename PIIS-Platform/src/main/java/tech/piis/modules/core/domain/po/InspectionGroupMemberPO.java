package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * 巡视组组员 对象 inspection_group_member
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_group_member")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionGroupMemberPO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "GROUP_MEMBER_ID", type = IdType.AUTO)
    private Long groupMemberId;
    /**
     * 巡视组ID
     **/
    private String groupId;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 组员ID
     */
    @NotBlank(message = "组员ID不能为空！")
    private String memberId;
    /**
     * 组员姓名
     */
    @NotBlank(message = "组员姓名不能为空！")
    private String memberName;
    /**
     * 所在公司ID
     */
    @NotBlank(message = "组员所在公司ID不能为空！")
    private String memberCompany;
    /**
     * 所在公司
     */
    @NotBlank(message = "组员所在公司名称不能为空！")
    private String memberCompanyName;
    /**
     * 担任职务
     */
//    @NotBlank(message = "组员职务不能为空！")
    private String memberPost;
    /**
     * 联系方式
     */
//    @NotBlank(message = "组员联系方式不能为空！")
    private String contact;

    /**
     * 被巡视单位
     */
    @TableField(exist = false)
    private List<InspectionUnitsPO> unitsList;

//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof InspectionGroupMemberPO) {
//            return Objects.equals(((InspectionGroupMemberPO) obj).getGroupMemberId(), this.groupMemberId);
//        }
//        return false;
//    }
//
//    public int hashCode() {
//        return Long.hashCode(groupMemberId);
//    }

}