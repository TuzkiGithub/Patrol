package tech.piis.modules.person.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * ClassName : LeadingGroupMemberPO
 * Package : tech.piis.modules.person.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("person_leading_group_member")
public class LeadingGroupMemberPO extends MABaseEntity{
    /**
     *  编号
     */
    @TableId(value = "leading_member_id")
    private String leadingMemberId;
    /**
     *
     * 人员编号
     */
    private String memberId;
    /**
     * 所属领导小组编号
     */
    private String leadingGroupId;
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
     * 不同机构所属人员数量
     */
    @TableField(exist = false)
    private Integer memberNum;

    /**
     * 任命文件
     */
    @TableField(exist = false)
    private List<PiisDocumentPO> piisDocumentPO;


}
