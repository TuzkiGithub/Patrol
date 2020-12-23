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
import tech.piis.modules.core.domain.vo.UserBriefVO;

import java.util.Date;
import java.util.List;

/**
 * 巡视报告对象 inspection_patrol_report
 *
 * @author Tuzki
 * @date 2020-12-11
 */

@TableName("inspection_patrol_report")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionPatrolReportPO extends PIBaseApprovalEntityPO {
    /**
     * 巡视报告编号
     */
    @TableId(value = "PATROL_REPORT_ID", type = IdType.AUTO)
    private Long patrolReportId;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 报告名称
     */
    private String reportName;
    /**
     * 报告时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;
    /**
     * 报告内容
     */
    private String reportContent;
    /**
     * 呈送人ID
     */
    private String forwardSendIds;
    /**
     * 呈送人姓名
     */
    private String forwardSendNames;

    @TableField(exist = false)
    private List<UserBriefVO> forwardSendList;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
