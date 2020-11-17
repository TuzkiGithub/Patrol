package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
     * 租户编号
     */
    private String entId;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    /**
     * 审批人ID
     */
    @TableField(exist = false)
    private String approverId;

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
