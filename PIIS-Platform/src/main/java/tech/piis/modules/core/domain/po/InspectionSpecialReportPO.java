package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SPECIAL_REPORT 对象 inspection_special_report
 *
 * @author Kevin
 * @date 2020-10-12
 */

@TableName("inspection_special_report")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionSpecialReportPO extends PIBaseApprovalEntityPO {
    /**
     * 专题报告
     */
    @TableId(value = "SPECIAL_REPORT_ID", type = IdType.AUTO)
    private Long specialReportId;

    /**
     * 巡视计划ID
     */
    @NotBlank(message = "巡视计划ID不能为空！")
    private String planId;

    /**
     * 巡视单位ID
     */
    @NotNull(message = "巡视单位ID不能为空！")
    private Long unitsId;
    /**
     * 报告类型
     */
    @NotBlank(message = "报告类型不能为空！")
    private String reportType;

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
