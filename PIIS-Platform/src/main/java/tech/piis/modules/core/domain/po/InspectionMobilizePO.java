package tech.piis.modules.core.domain.po;

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
 * 巡视动员 对象 inspection_mobilize
 *
 * @author Kevin
 * @date 2020-09-17
 */

@TableName("inspection_mobilize")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionMobilizePO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "MOBILIZE_ID")
    private String mobilizeId;
    /**
     * 计划编号
     */
    @NotNull(message = "巡视方案ID不能为空！")
    private String planId;
    /**
     * 会议名称
     */
    @NotBlank(message = "会议名称不能为空！")
    private String meetName;

    /**
     * 会议地点
     */
    @NotBlank(message = "会议地点不能为空！")
    private String meetPlace;

    /**
     * 会议时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "会议时间不能为空！")
    private Date meetTime;

    /**
     * 会议内容
     */
    private String meetDesc;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 租户编号
     */
    private String entId;

    /**
     * 巡视动员参会人员
     */
    @Valid
    @NotEmpty(message = "动员成员信息不能为空！")
    @TableField(exist = false)
    private List<InspectionMobilizeAttendeePO> inspectionMobilizeAttendeeList;

    /**
     * 会议通知文件信息
     */
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}