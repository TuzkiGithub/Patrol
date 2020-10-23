package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 查阅资料详情对象 inspection_consult_info_detail
 * 
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_consult_info_detail")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionConsultInfoDetailPO extends PIBaseEntity
{
    /** 查阅资料编号详情编号 */
    @TableId(value = "CONSULT_INFO_DETAIL_ID", type = IdType.AUTO)
    private Long consultInfoDetailId;
    /** 查阅资料编号 */
    private String consultInfoId;
    /** 调阅材料名称 */
    private String checkInfoName;
    /** 调阅份数 */
    private Long checkInfoCount;
    /** 纸质/电子 */
    private String checkInfoMaterialType;
    /** 原件/复印件 */
    private String checkInfoOriginType;
    /** 提供时限 */
    private String timeLimit;
    /** 提供人ID */
    private String providerId;
    /** 提供人姓名 */
    private String providerName;
    /** 提供时间 */
    private Date providerTime;
    /** 归还状态 */
    private Long returnStatus;
    /** 归还人ID */
    private String returnPersonId;
    /** 归还人姓名 */
    private String returnPersonName;
    /** 归还时间 */
    private Date returnTime;

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;

    /**
     * 操作类型
     *
     */
    @TableField(exist = false)
    private Integer operationType;
}
