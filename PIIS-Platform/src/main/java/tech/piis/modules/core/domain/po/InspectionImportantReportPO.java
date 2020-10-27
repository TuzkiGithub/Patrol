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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 重要情况专题报告 对象 inspection_important_report
 *
 * @author Kevin
 * @date 2020-10-23
 */

@TableName("inspection_important_report")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionImportantReportPO extends PIBaseEntity {
    /**
     * 重要情况专题报告编号
     */
    @TableId(value = "IMPORTANT_REPORT_ID", type = IdType.AUTO)
    private Long importantReportId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 报告名称
     */
    @NotEmpty(message = "报告名称不能为空！")
    private String reportName;
    /**
     * 报告时间
     */
    @NotNull(message = "报告时间不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;
    /**
     * 呈送人ID
     */
    @NotEmpty(message = "呈送人ID不能为空！")
    private String reporterId;
    /**
     * 呈送人姓名
     */
    @NotEmpty(message = "呈送人姓名不能为空！")
    private String reporterName;
    /**
     * 报告内容
     */
    private String reportContent;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
