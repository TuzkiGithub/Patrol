package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 组织会议对象 inspection_organization_mettings
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_organization_meetings")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionOrganizationMeetingsPO extends PIBaseEntity {
    /**
     * 组织编号
     */
    @TableId(value = "ORGANIZATION_MEETINGS_ID", type = IdType.AUTO)
    private Long organizationMeetingsId;
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
    @NotBlank(message = "会议名称不能为空！")
    private String meetingsName;
    /**
     * 会议时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "会议时间不能为空！")
    private Date meetingsTime;
    /**
     * 会议地点
     */
    @NotBlank(message = "会议地点不能为空！")
    private String meetingsPlace;
    /**
     * 参会人员ID
     */
    private String participantsId;
    /**
     * 参会人员姓名
     */
    private String participantsName;
    /**
     * 组织类型 0组务会，1支部会
     */
    @NotNull(message = "组织类型不能为空！")
    private Integer organizationType;
    /**
     * 会议内容
     */
    @NotBlank(message = "会议内容不能为空！")
    private String meetingsContent;

    /**
     * 参会人员LIST
     */
    @Valid
    @TableField(exist = false)
    @NotEmpty(message = "参会人员不能为空！")
    private List<UserBriefVO> participants;

    /**
     * 文件信息
     */
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
