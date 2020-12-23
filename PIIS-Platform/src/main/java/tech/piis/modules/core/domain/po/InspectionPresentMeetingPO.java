package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 列席会议 对象 inspection_present_meeting
 *
 * @author Tuzki
 * @date 2020-12-07
 */

@TableName("inspection_present_meeting")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionPresentMeetingPO extends PIBaseApprovalEntityPO {
    /**
     * 列席会议编号
     */
    @TableId(value = "PRESENT_MEETING_ID", type = IdType.AUTO)
    private Long presentMeetingId;
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
    private String reportName;
    /**
     * 报告时间
     */
    private Date reportTime;
    /**
     * 会议地点
     */
    private String venue;
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
     * 会议内容
     */
    private String meetingDesc;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;

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
}
