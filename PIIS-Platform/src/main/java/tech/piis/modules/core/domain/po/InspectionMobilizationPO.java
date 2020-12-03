package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 巡视动员会对象 inspection_mobilization
 *
 * @author Tuzki
 * @date 2020-11-25
 */

@TableName("inspection_mobilization")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionMobilizationPO extends PIBaseApprovalEntityPO {
    /**
     * 巡视动员会编号
     */
    @TableId(value = "MOBILIZATION_ID", type = IdType.AUTO)
    private Long mobilizationId;
    /**
     * 计划编号
     */
    @NotBlank(message = "巡视计划不能为空！")
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
    private String meetingName;
    /**
     * 人员范围ID
     */
    private String rangeId;
    /**
     * 人员范围姓名
     */
    private String rangeName;

    @TableField(exist = false)
    @NotEmpty(message = "人员范围不能为空！")
    private List<UserBriefVO> rangeList;
    /**
     * 审批状态 1：无需审批，2：待提交审批，3：审批中，4：已通过，5：已驳回
     */
    private Integer approvalFlag;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
