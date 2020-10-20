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
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 参会情况对象 inspection_attendance
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_attendance")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionAttendancePO extends PIBaseEntity {
    /**
     * 参会情况编号
     */
    @TableId(value = "ATTENDANCE_ID", type = IdType.AUTO)
    private Long attendanceId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private String unitsId;
    /**
     * 会议名称
     */
    private String meetingsName;
    /**
     * 会议时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date meetingsReportTime;
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

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
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
