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
 * 整改公开情况对象 inspection_rectification
 *
 * @author Tuzki
 * @date 2020-12-11
 */

@TableName("inspection_rectification")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionRectificationPO extends PIBaseApprovalEntityPO {
    /**
     * 整改公开情况编号
     */
    @TableId(value = "RECTIFICATION_ID", type = IdType.AUTO)
    private Long rectificationId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
