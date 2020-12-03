package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 临时支部成员 对象 inspection_temp_branch_member
 *
 * @author Tuzki
 * @date 2020-11-23
 */

@TableName("inspection_temp_branch_member")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionTempBranchMemberPO extends PIBaseEntity {
    /**
     * 临时支部成员编号
     */
    @TableId(value = "TEMP_BRANCH_MEMBER_ID", type = IdType.AUTO)
    private Long tempBranchMemberId;

    private String tempBranchId;
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
    private String memberCompanyId;
    /**
     * 所在公司名称
     */
    private String memberCompanyName;
    /**
     * 担任职务
     */
    private String memberPost;
    /**
     * 联系方式
     */
    private String memberContact;
}
