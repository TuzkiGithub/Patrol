package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * SINKNG_UNDERSTANDING_DETAIL 对象 inspection_sinkng_understanding_detail
 * 
 * @author Kevin
 * @date 2020-10-12
 */

@TableName("inspection_sinking_understanding_detail")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionSinkingUnderstandingDetailPO extends PIBaseEntity
{
    /** 下沉了解详情编号 */
    @TableId(value = "SINKING_UNDERSTANDING_DETAIL_ID", type = IdType.AUTO)
    private Long sinkingUnderstandingDetailId;
    /** 下沉了解编号 */
    private String sinkingUnderstandingId;
    /** 姓名 */
    private String understandingName;
    /** 所在公司ID */
    private String companyId;
    /** 所在公司名称 */
    private String companyName;
    /** 担任职务名称 */
    private String postName;
    /** 了解结果 */
    private String understandingContent;

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
