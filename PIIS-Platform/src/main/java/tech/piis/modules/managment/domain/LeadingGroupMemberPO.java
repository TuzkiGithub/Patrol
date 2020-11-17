package tech.piis.modules.managment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName : LeadingGroupMemberPO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("leading_group_member")
public class LeadingGroupMemberPO extends MABaseEntity{
    /**
     *
     * 人员编号
     */
    @TableId("member_id")
    private String memberId;
    /**
     * 所属领导小组编号
     */
    private Long leadingGroupId;
    /**
     * 机构编号
     */
    private String orgId;
    /**
     * 人员姓名
     */
    private String memberName;
    /**
     * 人员职务
     */
    private String memberPost;
    /**
     * 角色
     */
    private String memberRole;
    /**
     * 专兼职
     */
//    private String fullOrPart;
    /**
     * 任命文件编号
     */
    private String appointDocumentId;

//    private String memberType;
    /**
     * 不同机构所属人员数量
     */
    @TableField(exist = false)
    private Integer memberNum;
}
