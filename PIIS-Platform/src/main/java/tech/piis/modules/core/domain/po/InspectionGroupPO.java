package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 巡视组信息 对象 inspection_group
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_group")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionGroupPO extends PIBaseEntity {

    /**
     * 编号
     */
    @TableId(value = "GROUP_ID")
    private String groupId;

    /**
     * 计划编号
     */
    private String planId;

    /**
     * 巡视组名
     */
    @NotBlank(message = "巡视组名称不能为空！")
    private String groupName;

    /**
     * 组长ID
     */
    @NotBlank(message = "巡视组组长ID不能为空！")
    private String leaderId;

    /**
     * 组长
     */
    @NotBlank(message = "巡视组组长名称不能为空！")
    private String leaderName;

    /**
     * 副组长ID
     */
    @NotBlank(message = "巡视组副组长ID不能为空！")
    private String deputyLeaderId;

    /**
     * 副组长
     */
    @NotBlank(message = "巡视组副组长名称不能为空！")
    private String deputyLeaderName;

    /**
     * 被巡视单位
     */
    @Valid
    @NotEmpty(message = "被巡视单位信息不能为空！")
    private List<InspectionUnitsPO> inspectionUnitsList;

    /**
     * 巡视组组员
     */
    @Valid
    @NotEmpty(message = "巡视组组员信息不能为空！")
    private List<InspectionGroupMemberPO> inspectionGroupMemberList;

    //以上为持久层属性
    //////////////////////////////////////////////////////////////////////
    /**
     * 业务字段，表示巡视组操作类型
     *
     * 1：新增  2：修改  3：删除
     */
    @TableField(exist = false)
    private Integer operationType;
}