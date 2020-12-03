package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 报请审批 对象 inspection_report_approval
 *
 * @author Tuzki
 * @date 2020-11-23
 */

@TableName("inspection_report_approval")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionReportApprovalPO extends PIBaseEntity {
    /**
     * 报请申请编号
     */
    @TableId(value = "REPORT_APPROVAL_ID", type = IdType.AUTO)
    private Long reportApprovalId;
    /**
     * 计划ID
     */
    private String planId;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
