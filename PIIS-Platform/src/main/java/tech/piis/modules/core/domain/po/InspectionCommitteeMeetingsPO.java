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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 党委会小组会纪要 对象 inspection_committee_meetings
 *
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_committee_meetings")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionCommitteeMeetingsPO extends PIBaseEntity {
    /**
     * 党委会编号
     */
    @TableId(value = "COMMITTEE_MEETINGS_ID", type = IdType.AUTO)
    private Long committeeMeetingsId;
    /**
     * 巡视方案ID
     */
    @NotBlank(message = "巡视计划ID不能为空！")
    private String planId;
    /**
     * 被巡视单位ID
     */
    @NotNull(message = "被巡视单位ID不能为空！")
    private Long unitsId;
    /**
     * 会议名称
     */
    @NotBlank(message = "会议名称不能为空！")
    private String meetingsName;
    /**
     * 会议时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "会议时间不能为空！")
    private Date meetingsTime;

    /**
     * 议题名称
     */
    @NotBlank(message = "议题名称不能为空！")
    private String topicName;
    /**
     * 会议地点
     */
    @NotBlank(message = "会议地点不能为空！")
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

    @Valid
    @NotEmpty(message = "参会人员不能为空！")
    @TableField(exist = false)
    private List<UserBriefVO> participantList;
}
