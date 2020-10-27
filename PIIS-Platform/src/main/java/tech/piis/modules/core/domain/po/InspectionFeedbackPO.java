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
public class InspectionFeedbackPO extends PIBaseEntity
{
    /** 反馈意见编号 */
    private String feedbackId;
    /** 巡视方案ID */
    private String planId;
    /** 被巡视单位ID */
    private Long unitsId;
    /** 文件名称 */
    private String fileName;
    /** 发文编号 */
    private String fileCode;
    /** 主送单位ID */
    private String mainCompanyId;
    /** 主送单位名称 */
    private String mainCompanyName;
    /** 抄送单位ID */
    private String copyCompanyId;
    /** 抄送单位名称 */
    private String copyCompanyName;
    /** 待办时限 */
    private Date toDoTime;
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
