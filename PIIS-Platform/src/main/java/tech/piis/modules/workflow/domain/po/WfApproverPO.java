package tech.piis.modules.workflow.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 审批人员 对象 wf_approver
 *
 * @author Tuzki
 * @date 2020-11-16
 */

@TableName("wf_approver")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class WfApproverPO {
    /**
     * 审批人员编号
     */
    private Long approverId;
    /**
     * 工作流编号
     */
    private Long workflowId;
    /**
     * 节点编号
     */
    private Long nodeId;
    /**
     * 人员ID
     */
    private String userId;
    /**
     * 人员姓名
     */
    private String userName;


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
     * 租户编号
     */
    private String entId;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    /**
     * 审批人姓名
     */
    @TableField(exist = false)
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
