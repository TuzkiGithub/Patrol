package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
public class InspectionClueTransferPO extends PIBaseApprovalEntityPO {
    /**
     * 线索移交编号
     */
    @TableId(value = "CLUE_TRANSFER_ID")
    private String clueTransferId;
    /**
     * 巡视方案ID
     */
    @NotBlank(message = "巡视方案ID不能为空")
    private String planId;
    /**
     * 被巡视单位ID
     */
    @NotNull(message = "被巡视单位ID不能为空")
    private Long unitsId;
    /**
     * 问题线索名称
     */
    @NotBlank(message = "问题线索名称不能为空")
    private String clueName;
    /**
     * 移交方公司ID
     */
    @NotBlank(message = "移交方公司ID不能为空")
    private String transferCompanyId;
    /**
     * 移交方公司姓名
     */
    @NotBlank(message = "移交方公司姓名不能为空")
    private String transferCompanyName;
    /**
     * 移交经办人ID
     */
    @NotBlank(message = "移交经办人ID不能为空")
    private String transferAgentId;
    /**
     * 移交经办人姓名
     */
    @NotBlank(message = "移交经办人姓名不能为空")
    private String transferAgentName;
    /**
     * 移交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "移交时间不能为空")
    private Date transferTime;
    /**
     * 接收方公司ID
     */
    @NotBlank(message = "接收方公司ID不能为空")
    private String receiverCompanyId;
    /**
     * 接收方公司姓名
     */
    @NotBlank(message = "接收方公司姓名不能为空")
    private String receiverCompanyName;
    /**
     * 接收经办人ID
     */
    @NotBlank(message = "接收经办人ID不能为空")
    private String receiverAgentId;
    /**
     * 接收经办人姓名
     */
    @NotBlank(message = "接收经办人姓名不能为空")
    private String receiverAgentName;
    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "接收时间不能为空")
    private Date receiverTime;
    /**
     * 反馈部门ID
     */
    @NotBlank(message = "反馈部门ID不能为空")
    private String feedbackCompanyId;
    /**
     * 反馈部门名称
     */
    @NotBlank(message = "反馈部门名称不能为空")
    private String feedbackCompanyName;
    /**
     * 反馈时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "反馈时间不能为空")
    private Date feedbackTime;
    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 备注
     */
    private String remark;

    /**
     * 线索移交详情
     */
    @Valid
    private List<InspectionClueTransferDetailPO> clueTransferDetailList;


    /**
     * 操作类型
     */
    @TableField(exist = false)
    private Integer operationType;

}
