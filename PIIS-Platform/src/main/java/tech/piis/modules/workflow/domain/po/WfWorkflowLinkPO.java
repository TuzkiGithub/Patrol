package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * 流程线对象 wf_workflow_link
 *
 * @author Tuzki
 * @date 2020-11-16
 */

@TableName("wf_workflow_link")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfWorkflowLinkPO extends PIBaseEntity {
    /**
     * 流程线编号
     */
    private Long workflowLinkId;
    /**
     * 流程编号
     */
    private String workflowId;
    /**
     * 流程线名称
     */
    private String workflowLinkName;
    /**
     * 流程上一节点编号，0表示当前节点为初始节点
     */
    private String preNodeId;
    /**
     * 流程下一节点编号，-1表示当前节点为结束节点
     */
    private String nextNodeId;

    /**
     * 流程上一节点实体
     */
    private WfWorkflowNodePO preNode;

    /**
     * 流程下一节点实体
     */
    private WfWorkflowNodePO nextNode;
}
