package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseApprovalEntityPO;
import tech.piis.modules.core.domain.po.PIBaseEntity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.domain.po
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 14:58
 * Description:代办实体类
 */

@TableName("wf_workflow_todo")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfWorkFlowTodoPO{

    /**
     * 代办ID
     */
    @TableId(value = "WORK_FLOW_TODO_ID", type = IdType.AUTO)
    private Long workflowTodoId;

    /**
     * 业务ID
     */
    private String businessId;

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
     *  0代办，1已办
     */
    private Integer todoStatus;

    /**
     * 查看状态
     *  0未查看，1已查看
     */
    private Integer lookStatus;

    /**
     * 是否同意状态
     * 0：不同意
     * 1：同意
     *
     */
    private Integer agreeFlag;

    /**
     * 是否继续审批
     * 0无需继续审批 1：需要上级继续审批
     */
    private Integer continueApprovalFlag;

    /**
     * 审批意见
     */
    private String suggestion;

    /**
     * 扩展字段
     */
    private String remark;

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    /**
     * 分页参数-分页下标
     */
    @TableField(exist = false)
    private Integer pageNum;

    /**
     * 分页参数-每页数量
     */
    @TableField(exist = false)
    private Integer pageSize;

    /**
     * 租户编号
     */
    private String entId;

    /**
     * 业务表单数据
     */
    @TableField(exist = false)
    private Object bizObject;

    /**
     * 是否需要审批
     * 0：不需要
     * 1：需要
     */
    @TableField(exist = false)
    private Integer isApproval;

    /**
     * 是否为新增
     * 0 不是
     * 1 是
     */
    @TableField(exist = false)
    private Integer saveFlag;

    /**
     * 是否为提交
     * false 非提交
     * true  提交
     */
    @TableField(exist = false)
    private boolean submitFlag;

}
