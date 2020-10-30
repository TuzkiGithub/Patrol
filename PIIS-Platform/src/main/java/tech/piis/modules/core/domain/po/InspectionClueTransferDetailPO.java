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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
public class InspectionClueTransferDetailPO extends PIBaseEntity {
    /**
     * 线索移交详情编号
     */
    @TableId(value = "CLUE_TRANSFER_DETAIL_ID", type = IdType.AUTO)
    private Long clueTransferDetailId;
    /**
     * 线索移交编号
     */
    private String clueTransferId;
    /**
     * 被反映人姓名
     */
    @NotBlank(message = "被反映人姓名不能为空")
    private String reflectedPersonName;
    /**
     * 被反映人编号
     */
    @NotBlank(message = "被反映人编号不能为空")
    private String reflectedPersonId;
    /**
     * 被反映人职务
     */
    @NotBlank(message = "被反映人职务不能为空")
    private String reflectedPersonPost;
    /**
     * 线索来源
     */
    private String clueOrigin;
    /**
     * 受理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "受理时间不能为空")
    private Date acceptTime;
    /**
     * 拟移交单位ID
     */
    @NotBlank(message = "拟移交单位ID不能为空")
    private String planTransferCompanyId;
    /**
     * 拟移交单位名称
     */
    @NotBlank(message = "拟移交单位名称不能为空")
    private String planTransferCompanyName;

    /**
     * 文件信息
     */
    private List<PiisDocumentPO> documents;

    /**
     * 操作类型
     */
    @TableField(exist = false)
    private Integer operationType;
}
