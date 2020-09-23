package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 巡视动员 对象 inspection_mobilize
 *
 * @author Kevin
 * @date 2020-09-17
 */

@TableName("inspection_mobilize")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionMobilizePO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "MOBILIZE_ID", type = IdType.AUTO)
    private Long mobilizeId;
    /**
     * 计划编号
     */
    private String planId;
    /**
     * 会议名称
     */
    private String meetName;
    /**
     * 会议地点
     */
    private String meetPlace;
    /**
     * 会议时间
     */
    private Date meetTime;
    /**
     * 会议内容
     */
    private String meetDesc;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    private Date updatedTime;
    /**
     * 租户编号
     */
    private String entId;

    /**
     * 巡视动员参会人员
     */
    private List<InspectionMobilizeAttendeePO> inspectionMobilizeAttendeeList;
}