package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UnitsBriefVO;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 反馈意见 对象 inspection_feedback
 *
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_feedback")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionFeedbackPO extends PIBaseEntity {
    /**
     * 反馈意见编号
     */
    @TableId(value = "FEEDBACK_ID")
    private String feedbackId;
    /**
     * 巡视方案ID
     */
    @NotBlank(message = "巡视方案ID不能为空!")
    private String planId;
    /**
     * 被巡视单位ID
     */
    @NotNull(message = "被巡视单位ID不能为空!")
    private Long unitsId;
    /**
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空!")
    private String fileName;
    /**
     * 发文编号
     */
    @NotBlank(message = "发文编号不能为空!")
    private String fileCode;
    /**
     * 主送单位ID
     */
    @NotBlank(message = "主送单位ID不能为空!")
    private String mainCompanyId;
    /**
     * 主送单位名称
     */
    @NotBlank(message = "主送单位名称不能为空!")
    private String mainCompanyName;
    /**
     * 抄送单位ID
     */
    private String copyCompanyId;
    /**
     * 抄送单位名称
     */
    private String copyCompanyName;
    /**
     * 待办时限
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date toDoTime;

    /**
     * 印发时间
     */
    @NotNull(message = "印发时间不能为空!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date printTime;

    @Valid
    /**
     * 整改文件列表
     */
    private List<InspectionFeedbackQuestionsPO> feedbackQuestionsList;

    @Valid
    @TableField(exist = false)
    @NotEmpty(message = "抄送单位不能为空!")
    private List<UnitsBriefVO> copyUnitsList;
}
