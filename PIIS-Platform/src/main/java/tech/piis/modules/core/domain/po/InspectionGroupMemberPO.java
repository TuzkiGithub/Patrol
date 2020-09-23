package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 巡视组组员 对象 inspection_group_member
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_group_member")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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
    private Long groupId;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 组员ID
     */
    private String memberId;
    /**
     * 组员姓名
     */
    private String memberName;
    /**
     * 所在公司ID
     */
    private String memberCompany;
    /**
     * 所在公司
     */
    private String memberCompanyName;
    /**
     * 担任职务
     */
    private String memberPost;
    /**
     * 联系方式
     */
    private String contact;
}