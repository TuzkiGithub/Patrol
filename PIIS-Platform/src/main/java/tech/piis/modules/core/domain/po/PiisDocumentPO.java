package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

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
     * 巡视编号
     */
    private String piisId;
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

}