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
 * 印发通知 对象 inspection_issuance_notice
 *
 * @author Tuzki
 * @date 2020-11-23
 */

@TableName("inspection_issuance_notice")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionIssuanceNoticePO extends PIBaseEntity {
    /**
     * 印发通知编号
     */
    @TableId(value = "ISSUANCE_NOTICE_ID", type = IdType.AUTO)
    private Long issuanceNoticeId;
    /**
     * 计划编号
     */
    private String planId;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
