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
 * 线索移交详情 对象 inspection_clue_transfer_detail
 * 
 * @author Tuzki
 * @date 2020-10-27
 */

@TableName("inspection_clue_transfer_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionClueTransferDetailPO extends PIBaseEntity
{
    /** 线索移交详情编号 */
    private Long clueTransferDetailId;
    /** 线索移交编号 */
    private String clueTransferId;
    /** 被反映人姓名 */
    private String reflectedPersonName;
    /** 被反映人编号 */
    private String reflectedPersonId;
    /** 被反映人职务 */
    private String reflectedPersonPost;
    /** 线索来源 */
    private String clueOrigin;
    /** 受理时间 */
    private Date acceptTime;
    /** 拟移交单位ID */
    private String transferCompanyId;
    /** 拟移交单位名称 */
    private String transferCompanyName;
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
