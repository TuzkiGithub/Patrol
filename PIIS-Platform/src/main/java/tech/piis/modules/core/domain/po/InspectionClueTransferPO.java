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
 * 线索移交 对象 inspection_clue_transfer
 * 
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_clue_transfer")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionClueTransferPO extends PIBaseEntity
{
    /** 线索移交编号 */
    private String clueTransferId;
    /** 巡视方案ID */
    private String planId;
    /** 被巡视单位ID */
    private Long unitsId;
    /** 问题线索名称 */
    private String clueName;
    /** 移交方ID */
    private String transferId;
    /** 移交方姓名 */
    private String transferName;
    /** 移交经办人ID */
    private String transferAgentId;
    /** 移交经办人姓名 */
    private String transferAgentName;
    /** 移交时间 */
    private Date transferTime;
    /** 接收方ID */
    private String receiverId;
    /** 接收方姓名 */
    private String receiverName;
    /** 接收经办人ID */
    private String receiverAgentId;
    /** 接收经办人姓名 */
    private String receiverAgentName;
    /** 接收时间 */
    private Date receiverTime;
    /** 反馈部门ID */
    private String feedbackCompanyId;
    /** 反馈部门名称 */
    private String feedbackCompanyName;
    /** 反馈时间 */
    private Date feedbackTime;
    /** 反馈内容 */
    private String feedbackContent;
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
