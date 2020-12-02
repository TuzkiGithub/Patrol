package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.po
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 17:07
 * Description:
 */
@Data
@Accessors(chain = true)
public class PIBaseApprovalEntityPO {
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
     * 审批人ID
     */
    private String approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 审批状态 1：无需审批，2：待提交审批，3：审批中，4：已通过，5：已驳回'
     */
    private Integer approvalFlag;

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

}
