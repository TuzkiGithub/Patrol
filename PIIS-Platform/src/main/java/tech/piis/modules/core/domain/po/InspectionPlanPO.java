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
 * 巡视计划 对象 inspection_plan
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_plan")
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class InspectionPlanPO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "PLAN_ID")
    private String planId;
    /**
     * 方案名称
     */
    @NotBlank(message = "巡视方案不能为空！")
    private String planName;
    /**
     * 预计开始时间
     */
    @NotNull(message = "巡视开始时间不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 预计结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 巡视状态 0未开始，1进行中，2结束
     */
    private int status;
    /**
     * 方案内容
     */
    private String planDesc;

    /**
     * 巡视巡察状态 0 巡视 1 巡察
     */
    private int planType;


    /**
     * 巡视单位ID
     */
    private String planCompanyId;

    /**
     * 巡视单位名称
     */
    private String planCompanyName;

    /**
     * 巡视组
     */
    @Valid
    @NotEmpty(message = "巡视组信息不能为空！")
    private List<InspectionGroupPO> inspectionGroupList;


    /**
     * 文件列表
     */
    @NotEmpty(message = "文件信息不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;

    /**
     * 巡视动员
     */
    private InspectionMobilizePO inspectionMobilize;
}