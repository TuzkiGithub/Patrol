package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 被巡视单位 对象 inspection_units
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_units")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionUnitsPO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "UNITS_ID", type = IdType.AUTO)
    private Long unitsId;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 巡视组ID
     */
    private Long groupId;
    /**
     * 巡视时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date inspectionTime;
    /**
     * 部门ID
     */
    private String orgId;
    /**
     * 部门名称
     */
    private String orgName;
}