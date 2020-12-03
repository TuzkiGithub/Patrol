package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 巡视附件 对象 piis_document
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("piis_document")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PiisDocumentPO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "PIIS_DOC_ID", type = IdType.AUTO)
    private Long piisDocId;

    /**
     * 业务编号
     */
    private String objectId;

    /**
     * 文件字典ID
     */
    private Long fileDictId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件路径
     */
    private String filePath;

    /**  业务字段
     *   操作类型
     * 1 新增 3删除
     */
    @TableField(exist = false)
    private Integer operationType;
}