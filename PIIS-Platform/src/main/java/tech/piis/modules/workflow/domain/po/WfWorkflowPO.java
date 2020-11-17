package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * 流程 对象 wf_workflow
 *
 * @author Tuzki
 * @date 2020-11-16
 */

@TableName("wf_workflow")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfWorkflowPO extends PIBaseEntity {
    /**
     * 流程编号
     */
    private Long workflowId;
    /**
     * 流程名称
     */
    private String workflowName;
    /**
     * 流程描述
     */
    private String workflowDesc;

}
