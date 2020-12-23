package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.vo.UnitsBriefVO;
import tech.piis.modules.core.domain.vo.UserBriefVO;

import java.util.Date;
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

    /**
     * 标题
     */
    private String title;
    /**
     * 呈送人Id
     */
    private String forwardSendIds;

    /**
     * 呈送人姓名
     */
    private String forwardSendNames;
    /**
     * 经办人ID
     */
    private String agentId;
    /**
     * 经办人姓名
     */
    private String agentName;
    /**
     * 密级
     */
    private String classification;
    /**
     * 急缓
     */
    private String quickSlow;
    /**
     * 电话
     */
    private String phone;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date time;
    /**
     * 会签部门ID
     */
    private String signDeptIds;
    /**
     * 会签部门名称
     */
    private String signDeptNames;

    @TableField(exist = false)
    private List<UserBriefVO> forwardSendList;

    @TableField(exist = false)
    private List<UnitsBriefVO> signOrgList;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
