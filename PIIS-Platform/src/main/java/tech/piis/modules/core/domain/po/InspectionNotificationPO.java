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
 * 情况通报 对象 inspection_notification
 *
 * @author Tuzki
 * @date 2020-11-23
 */

@TableName("inspection_notification")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionNotificationPO extends PIBaseEntity {
    /**
     * 情况通报编号
     */
    @TableId(value = "NOTIFICATION_ID", type = IdType.AUTO)
    private Long notificationId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 通报人ID
     */
    private String notifierId;
    /**
     * 通报人姓名
     */
    private String notifierName;
    /**
     * 通报人职务
     */
    private String notifierPost;
    /**
     * 通报人公司ID
     */
    private String notifierCompanyId;
    /**
     * 通报人公司名称
     */
    private String notifierCompanyName;
    /**
     * 时间
     */
    private String time;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
