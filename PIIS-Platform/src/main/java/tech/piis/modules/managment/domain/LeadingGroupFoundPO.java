package tech.piis.modules.managment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * ClassName : LeadingGroupFoundPO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName(value = "leading_group_found")
public class LeadingGroupFoundPO extends MABaseEntity{
    /**
     * 巡察领导小组编号
     */
    @TableId(value = "leading_group_id",type = IdType.AUTO)
    private Long leadingGroupId;

    /**
     * 所属机构编号
     */
    @NotNull(message = "所属机构编号不能为空")
    private String orgId;

    /**
     * 所属机构名称
     */
    private String orgName;

    /**
     * 巡察办成立时间
     */
    @NotEmpty(message = "成立时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date foundTime;

    /**
     * 成立文件编号
     */
    @NotEmpty(message = "成立文件编号不能为空")
    private String documentId;

//    @TableField(exist = false)
//    @NotEmpty(message = "成立文件编号不能为空")
//    private List<String> documentIdList;

    /**
     * 所属领导小组成员
     */
    @TableField(exist = false)
    private List<LeadingGroupMemberPO> memberPOList;

}
