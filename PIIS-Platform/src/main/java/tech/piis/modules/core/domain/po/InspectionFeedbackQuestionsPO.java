package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 反馈问题清单 对象 inspection_feedback_questions
 *
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_feedback_questions")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionFeedbackQuestionsPO extends PIBaseEntity {
    /**
     * 反馈问题清单编号
     */
    @TableId(value = "FEEDBACK_QUESTIONS_ID", type = IdType.AUTO)
    private Long feedbackQuestionsId;
    /**
     * 反馈问题ID
     */
    private String feedbackId;
    /**
     * 问题归类
     */
    @NotBlank(message = "问题归类不能为空!")
    private String problemClassification;
    /**
     * 问题表现
     */
    @NotBlank(message = "问题表现不能为空!")
    private String problemPerformance;
    /**
     * 分管领导ID
     */
    @NotBlank(message = "分管领导ID不能为空!")
    private String deputyLeaderId;
    /**
     * 分管领导名称
     */
    @NotBlank(message = "分管领导名称不能为空!")
    private String deputyLeaderName;
    /**
     * 牵头整改部门ID
     */
    @NotBlank(message = "牵头整改部门ID不能为空!")
    private String rectificationCompanyId;
    /**
     * 牵头整改部门名称
     */
    @NotBlank(message = "牵头整改部门名称不能为空!")
    private String rectificationCompanyName;
    /**
     * 主要负责人ID
     */
    @NotBlank(message = "主要负责人ID不能为空!")
    private String mainLeaderId;
    /**
     * 主要负责人名称
     */
    @NotBlank(message = "主要负责人名称不能为空!")
    private String mainLeaderName;
    /**
     * 整改状态
     */
    @NotBlank(message = "整改状态不能为空!")
    private String rectificationStatus;
    /**
     * 整改措施
     */
    private String rectificationMeasures;

    /**
     * 操作类型
     */
    @TableField(exist = false)
    private Integer operationType;
}
