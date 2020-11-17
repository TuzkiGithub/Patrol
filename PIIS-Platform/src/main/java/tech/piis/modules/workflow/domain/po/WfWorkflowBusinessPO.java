package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * 流程业务 对象 wf_workflow_business
 *
 * @author Tuzki
 * @date 2020-11-16
 */

@TableName("wf_workflow_business")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfWorkflowBusinessPO extends PIBaseEntity {
    /**
     * 业务流程编号
     */
    private Long workflowBusinessId;
    /**
     * 流程编号
     */
    private String businessId;
    /**
     * 流程节点编号
     */
    private Long nodeId;
    /**
     * 审批建议
     */
    private String suggestion;
    /**
     * 审批人ID
     */
    private String approverId;
    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 是否需要审批
     * 0：不需要
     * 1：需要
     */
//    @NotNull(message = "是否需要审批不能为空！")
    @TableField(exist = false)
    private Integer approvalFlag;
}
