package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    private Long mobilizeAttendeeId;
    /**
     * 动员编号
     */
    private String mobilizeId;
    /**
     * 参会人员ID
     */
    @NotBlank(message = "动员编号不能为空！")
    private String attendeeId;
    /**
     * 参会人员姓名
     */
    @NotBlank(message = "动员姓名不能为空！")
    private String attendeeName;
    /**
     * 参会人员职务
     */
    @NotBlank(message = "动员职务不能为空！")
    private String attendeePost;
    /**
     * 参会人员公司名称
     */
    @NotBlank(message = "所在公司名称不能为空！")
    private String attendeeCompany;

    /**
     * 参会人员公司ID
     */
    @NotBlank(message = "所在公司ID不能为空！")
    private String attendeeCompanyId;

    /**
     * 授课时间
     */
    private String attendeeTime;

    /**
     * 动员人员类型
     * 0 普通人员
     * 1 授课老师
     * 2 领导
     */
    @NotNull(message = "人员类型不能为空！")
    private Integer attendeePersonType;

    /**
     * 备注
     */
    private String remark;

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
     * 业务字段，表示巡视组操作类型
     *
     * 1：新增  2：修改  3：删除
     */
    @TableField(exist = false)
    private Integer operationType;

    /**
     * 文件信息
     */
    private List<PiisDocumentPO> documents;
}