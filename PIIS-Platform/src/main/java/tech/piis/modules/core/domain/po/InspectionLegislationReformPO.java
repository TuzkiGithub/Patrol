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

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 立行立改 对象 inspection_legislation_reform
 *
 * @author Kevin
 * @date 2020-10-23
 */

@TableName("inspection_legislation_reform")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionLegislationReformPO extends PIBaseEntity {
    /**
     * 立行立改编号
     */
    @TableId(value = "LEGISLATION_REFORM_ID", type = IdType.AUTO)
    private Long legislationReformId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 立行立改名称
     */
    @NotEmpty(message = "立行立改名称不能为空！")
    private String legislationReformName;
    /**
     * 立行立改时间
     */
    @NotEmpty(message = "立行立改时间不能为空！")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date legislationReformTime;
    /**
     * 立行立改措施
     */
    private String legislationReformMeasures;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
