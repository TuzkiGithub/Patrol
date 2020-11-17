package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.domain.po
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 14:58
 * Description:代办实体类
 */

@TableName("wf_workflow")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfWorkFlowTodoPO extends PIBaseEntity {

    /**
     * 代办ID
     */
    @TableId(value = "WORK_FLOW_TODO_ID", type = IdType.AUTO)
    private Long workflowTodoId;

    /**
     * 代办名称
     */
    private String todoName;

    /**
     * 审批人ID
     */
    private String approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 代办状态
     */
    private Integer todoStatus;
}
