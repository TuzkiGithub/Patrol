package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 调研走访对象 inspection_investigation_visit
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_investigation_visit")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionInvestigationVisitPO extends PIBaseApprovalEntityPO {
    /**
     * 调研走访编号
     */
    @TableId(value = "INVESTIGATION_VISIT_ID")
    private String investigationVisitId;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 走访单位ID
     */
    private String visitCompanyId;
    /**
     * 走访单位名称
     */
    private String visitCompanyName;
    /**
     * 走访时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitTime;
    /**
     * 记录人ID
     */
    private String recordPersonId;
    /**
     * 记录人姓名
     */
    private String recordPersonName;
    /**
     * 记录内容
     */
    private String recordContent;

    /**
     * 人员信息
     */
    @NotEmpty(message = "文件不能为空！")
    private List<InspectionInvestigationVisitPersonPO> investigationVisitPersonList;

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
