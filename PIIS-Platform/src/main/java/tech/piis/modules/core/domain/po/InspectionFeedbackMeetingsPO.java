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

import java.util.Date;
import java.util.List;

/**
 * 反馈会对象 inspection_feedback_meetings
 *
 * @author Tuzki
 * @date 2020-12-11
 */

@TableName("inspection_feedback_meetings")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionFeedbackMeetingsPO extends PIBaseApprovalEntityPO {
    /**
     * 反馈会编号
     */
    @TableId(value = "FEEDBACK_MEETINGS_ID", type = IdType.AUTO)
    private Long feedbackMeetingsId;
    /**
     * 巡视方案ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 会议名称
     */
    private String meetingsName;
    /**
     * 会议时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date meetingsTime;
    /**
     * 会议地点
     */
    private String venue;
    /**
     * 参会人员ID
     */
    private String participantsId;
    /**
     * 参会人员姓名
     */
    private String participantsName;
    /**
     * 会议内容
     */
    private String meetingsContent;
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
