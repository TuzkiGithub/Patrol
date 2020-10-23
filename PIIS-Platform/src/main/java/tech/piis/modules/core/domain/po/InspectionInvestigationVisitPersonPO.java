package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 调研走访人员对象 inspection_investigation_visit_person
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_investigation_visit_person")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionInvestigationVisitPersonPO extends PIBaseEntity {
    /**
     * 调研走访人员编号
     */
    @TableId(value = "INVESTIGATION_VISIT_PERSON_ID", type = IdType.AUTO)
    private Long investigationVisitPersonId;
    /**
     * 调研走访编号
     */
    private String investigationVisitId;
    /**
     * 人员编号
     */
    private String personId;
    /**
     * 人员名称
     */
    private String personName;
    /**
     * 职务名称
     */
    private String postName;
    /**
     * 人员类型 0出访人，1被访人
     */
    private Integer personType;

    /**
     * 操作类型
     */
    @TableField(exist = false)
    private Integer operationType;

}
