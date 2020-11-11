package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 语录对象 inspection_quotation
 *
 * @author Tuzki
 * @date 2020-11-06
 */

@TableName("inspection_quotation")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionQuotationPO extends PIBaseEntity {
    /**
     * 语录编号
     */
    @TableId(value = "QUOTATION_ID", type = IdType.AUTO)
    private Long quotationId;
    /**
     * 语录内容
     */
    @NotBlank(message = "语录内容不能为空！")
    private String quotationContent;
    /**
     * 出自
     */
    @NotBlank(message = "出自不能为空！")
    private String origin;
    /**
     * 名人
     */
    @NotBlank(message = "名人不能为空！")
    private String speaker;
    /**
     * 排序
     */
    @NotNull(message = "排序不能为空！")
    private Integer sort;
}
