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
 * 问题底稿 对象 inspection_problem_draft
 *
 * @author Kevin
 * @date 2020-10-27
 */

@TableName("inspection_problem_draft")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionProblemDraftPO extends PIBaseApprovalEntityPO {
    /**
     * 问题底稿编号
     */
    @TableId(value = "PROBLEM_DRAFT_ID", type = IdType.AUTO)
    private Long problemDraftId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 底稿名称
     */
    private String draftName;
    /**
     * 底稿编号
     */
    private String draftCode;
    /**
     * 问题来源
     */
    private String problemOrigin;
    /**
     * 承办人ID
     */
    private String underTakerId;
    /**
     * 承办人姓名
     */
    private String underTakerName;
    /**
     * 问题概述
     */
    private String problemDesc;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}