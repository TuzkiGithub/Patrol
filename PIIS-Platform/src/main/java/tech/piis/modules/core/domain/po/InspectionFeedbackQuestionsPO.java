package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import java.util.List;

import javax.validation.constraints.NotBlank;
import java.util.Date;

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
public class InspectionFeedbackQuestionsPO extends PIBaseEntity
{
    /** 反馈问题清单编号 */
    private Long feedbackQuestionsId;
    /** 反馈问题ID */
    private String feedbackId;
    /** 问题归类 */
    private String problemClassification;
    /** 问题表现 */
    private String peoblemPerformance;
    /** 分管领导ID */
    private String deputyLeaderId;
    /** 分管领导名称 */
    private String deputyLeaderName;
    /** 牵头整改部门ID */
    private String rectificationCompanyId;
    /** 牵头整改部门名称 */
    private String rectificationCompanyName;
    /** 主要负责人ID */
    private String mainLeaderId;
    /** 主要负责人名称 */
    private String mainLeaderName;
    /** 整改状态 */
    private Long rectificationStatus;
    /** 整改措施 */
    private String rectificationMeasures;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private Date createdTime;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private Date updatedTime;
    /** 租户编号 */
    private String entId;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
