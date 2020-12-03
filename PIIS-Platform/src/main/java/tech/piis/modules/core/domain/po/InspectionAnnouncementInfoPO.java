package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 公告信息 对象 inspection_announcement_info
 *
 * @author Tuzki
 * @date 2020-12-03
 */

@TableName("inspection_announcement_info")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionAnnouncementInfoPO extends PIBaseApprovalEntityPO {
    /**
     * 公告信息编号
     */
    @TableId(value = "ANNOUNCEMENT_INFO_ID", type = IdType.AUTO)
    private Long announcementInfoId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;

    private List<PiisDocumentPO> documents;
}
