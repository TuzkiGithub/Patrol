package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 被巡视单位 对象 inspection_units
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_units")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionUnitsPO extends PIBaseEntity {

    /**
     * 编号
     */
    @TableId(value = "UNITS_ID", type = IdType.AUTO)
    private Long unitsId;

    /**
     * 计划编号
     */
    private String planId;

    /**
     * 巡视组ID
     */
    private String groupId;

    /**
     * 巡视时间
     */
    @NotNull(message = "被巡视时间不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date inspectionTime;

    /**
     * 部门ID
     */
    @NotBlank(message = "部门ID不能为空！")
    private String orgId;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空！")
    private String orgName;

    /**
     * 巡视组组员
     */
    @Valid
    @NotEmpty(message = "组员信息不能为空！")
    private List<InspectionGroupMemberPO> groupMemberList;

}