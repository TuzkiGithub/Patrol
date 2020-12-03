package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 来访对象 inspection_visit
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_visit")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionVisitPO extends PIBaseApprovalEntityPO {
    /**
     * 来访编号
     */
    @TableId(value = "VISIT_ID", type = IdType.AUTO)
    private Long visitId;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 受理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitTime;
    /**
     * 被访人公司编号
     */
    private String visitCompanyId;
    /**
     * 被访人公司名称
     */
    private String visitCompanyName;
    /**
     * 被访人编号
     */
    private String visitPersonId;
    /**
     * 被访人姓名
     */
    private String visitPersonName;
    /**
     * 被访人职务
     */
    private String visitPersonPost;
    /**
     * 信访来源
     */
    private String visitOrigin;
    /**
     * 集团干部标志
     */
    private Integer cadreFlag;
    /**
     * 反映内容提要
     */
    private String visitSummary;
    /**
     * 经办人编号
     */
    private String agentId;
    /**
     * 经办人姓名
     */
    private String agentName;
    /**
     * 经办时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date agentTime;

    /**
     * 文件信息
     */
    @TableField(exist = false)
    @NotEmpty(message = "文件不能为空！")
    private List<PiisDocumentPO> documents;
}
