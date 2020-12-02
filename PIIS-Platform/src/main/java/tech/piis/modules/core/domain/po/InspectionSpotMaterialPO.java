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
 * 驻场材料 对象 inspection_spot_material
 *
 * @author Tuzki
 * @date 2020-11-23
 */

@TableName("inspection_spot_material")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionSpotMaterialPO extends PIBaseEntity {
    /**
     * 驻场材料编号
     */
    @TableId(value = "SPOT_MATERIAL_ID", type = IdType.AUTO)
    private Long spotMaterialId;
    /**
     * 计划ID
     */
    private String planId;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;

}
