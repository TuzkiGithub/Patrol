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
 * 党委会小组会纪要 对象 inspection_committee_meetings
 * 
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_committee_meetings")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionCommitteeMeetingsPO extends PIBaseEntity
{
    /** 党委会编号 */
    private Long committeeMeetingsId;
    /** 巡视方案ID */
    private String planId;
    /** 被巡视单位ID */
    private Long unitsId;
    /** 会议名称 */
    private String meetingsName;
    /** 会议时间 */
    private Date meetingsTime;
    /** 会议地点 */
    private String venue;
    /** 参会人员ID */
    private String participantsId;
    /** 参会人员姓名 */
    private String participantsName;
    /** 会议内容 */
    private String meetingsContent;
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
