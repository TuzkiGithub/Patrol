package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import java.util.List;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 个别谈话 对象 inspection_individual_talk
 * 
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_individual_talk")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionIndividualTalkPO extends PIBaseEntity
{
    /** 个别谈话编号 */
    private Long individualTalkId;
    /** 巡视计划ID */
    private String planId;
    /** 被巡视单位ID */
    private Long unitsId;
    /** 谈话对象ID */
    private String talkObjId;
    /** 谈话对象姓名 */
    private String talkObjName;
    /** 谈话对象职务名称 */
    private String talkObjPost;
    /** 谈话对象单位ID */
    private String talkCompanyId;
    /** 谈话对象单位名称 */
    private String talkCompanyName;
    /** 谈话人员ID */
    private String talkPersonId;
    /** 谈话人员姓名 */
    private String talkPersonName;
    /** 记录人员ID */
    private String recordPersonId;
    /** 记录人员姓名 */
    private String recordPersonName;
    /** 谈话时间 */
    private Date talkTime;
    /** 摘要分类 */
    private String abstractClassification;
    /** 摘要内容 */
    private String abstractContent;
    /** 谈话地点 */
    private String talkPlace;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private Date createdTime;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private Date updatedTime;
    /** 租户编号 */
    private String entId;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
