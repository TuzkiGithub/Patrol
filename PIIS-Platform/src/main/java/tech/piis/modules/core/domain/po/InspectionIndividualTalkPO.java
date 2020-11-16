package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 个别谈话 对象 inspection_individual_talk
 *
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_individual_talk")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionIndividualTalkPO extends PIBaseEntity {
    /**
     * 个别谈话编号
     */
    @TableId(value = "INDIVIDUAL_TALK_ID")
    private String individualTalkId;

    /**
     * 谈话编号
     */
    @NotBlank(message = "谈话编号不能为空！")
    private String individualTalkCode;
    /**
     * 巡视计划ID
     */
    @NotBlank(message = "巡视计划ID不能为空！")
    private String planId;
    /**
     * 被巡视单位ID
     */
    @NotNull(message = "被巡视单位ID不能为空！")
    private Long unitsId;
    /**
     * 谈话对象ID
     */
    @NotBlank(message = "谈话对象ID不能为空！")
    private String talkObjId;
    /**
     * 谈话对象姓名
     */
    @NotBlank(message = "谈话对象姓名不能为空！")
    private String talkObjName;
    /**
     * 谈话对象职务名称
     */
    @NotBlank(message = "谈话对象职务名称不能为空！")
    private String talkObjPost;
    /**
     * 谈话对象单位ID
     */
    @NotBlank(message = "谈话对象单位ID不能为空！")
    private String talkCompanyId;
    /**
     * 谈话对象单位名称
     */
    @NotBlank(message = "谈话对象单位名称不能为空！")
    private String talkCompanyName;

    /**
     * 谈话时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "谈话时间不能为空！")
    private Date talkTime;

    /**
     * 谈话地点
     */
    @NotBlank(message = "谈话地点不能为空！")
    private String talkPlace;

    /**
     * 谈话人员ID
     */
    private String talkPersonId;
    /**
     * 谈话人员姓名
     */
    private String talkPersonName;
    /**
     * 记录人员ID
     */
    private String recordPersonId;
    /**
     * 记录人员姓名
     */
    private String recordPersonName;

    /**
     * 谈话ID
     */
    private String talkOutlineIds;

    @NotEmpty(message = "谈话摘要不能为空！")
    List<InspectionTalkOutlinePO> talkOutlineList;

    @NotEmpty(message = "记录人员不能为空！")
    @TableField(exist = false)
    private List<UserBriefVO> recordList;

    @NotEmpty(message = "谈话人员不能为空！")
    @TableField(exist = false)
    private List<UserBriefVO> talkPersonList;
}
