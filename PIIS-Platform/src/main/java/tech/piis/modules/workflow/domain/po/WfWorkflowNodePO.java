package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * 流程节点 对象 wf_workflow_node
 *
 * @author Tuzki
 * @date 2020-11-16
 */

@TableName("wf_workflow_node")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfWorkflowNodePO extends PIBaseEntity {
    /**
     * 流程节点编号
     */
    private Long workflowNodeId;
    /**
     * 流程ID
     */
    private Long workflowId;
    /**
     * 节点名称
     */
    private String nodeName;

}
