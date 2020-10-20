package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
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
public class InspectionSpecialReportPO extends PIBaseEntity {
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
     * 报告名称
     */
    @NotBlank(message = "报告名称不能为空！")
    private String reportName;
    /**
     * 报告时间
     */

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "报告时间不能为空！")
    private Date reportTime;
    /**
     * 会议地点
     */
    @NotBlank(message = "会议地点不能为空！")
    private String venue;
    /**
     * 会议内容
     */
    private String meetingDesc;
    /**
     * 汇报人姓名
     */
    private String reporterName;
    /**
     * 汇报人ID
     */
    private String reporterId;
    /**
     * 巡视组参与人员姓名
     */
    private String inspectionGroupPersonName;
    /**
     * 巡视组参与人员ID
     */
    private String inspectionGroupPersonId;
    /**
     * 汇报会参加人员姓名
     */
    private String reportPersonName;
    /**
     * 汇报会参加人员ID
     */
    private String reportPersonId;
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

    ////////////////////////////////////非PO字段/////////////////////////////////////

    /**
     * 听取报告人员
     */
    @NotEmpty(message = "听取报告人员不能为空！")
    @TableField(exist = false)
    @Valid
    private List<UserBriefVO> reporter;

    /**
     * 巡视组人员
     */
    @NotEmpty(message = "巡视组人员信息不能为空！")
    @TableField(exist = false)
    @Valid
    private List<UserBriefVO> inspectionGroupPersons;

    /**
     * 参会人员
     */
    @NotEmpty(message = "参会人员不能为空！")
    @TableField(exist = false)
    @Valid
    private List<UserBriefVO> participants;

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
