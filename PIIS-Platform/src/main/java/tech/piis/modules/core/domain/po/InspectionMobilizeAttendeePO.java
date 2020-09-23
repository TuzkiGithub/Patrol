package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 巡视动员参会人员 对象 inspection_mobilize_attendee
 *
 * @author Kevin
 * @date 2020-09-17
 */

@TableName("inspection_mobilize_attendee")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionMobilizeAttendeePO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "MOBILIZE_ATTENDEE_ID", type = IdType.AUTO)
    private String mobilizeAttendeeId;
    /**
     * 动员编号
     */
    private String mobilizeId;
    /**
     * 参会人员ID
     */
    private String attendeeId;
    /**
     * 参会人员姓名
     */
    private String attendeeName;
    /**
     * 参会人员职务
     */
    private String attendeePost;
    /**
     * 参会人员公司
     */
    private String attendeeCompany;
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
}