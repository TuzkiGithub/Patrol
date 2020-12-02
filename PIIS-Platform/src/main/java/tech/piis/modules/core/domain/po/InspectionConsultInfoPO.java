package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 查阅资料对象 inspection_consult_info
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_consult_info")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionConsultInfoPO extends PIBaseApprovalEntityPO {
    /**
     * 查阅资料编号
     */
    @TableId(value = "CONSULT_INFO_ID")
    private String consultInfoId;
    /**
     * 巡视计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /**
     * 组长是否签字
     */
    private Integer signFlag;
    /**
     * 签字时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date signTime;
    /**
     * 调阅人ID
     */
    private String checkPersonId;
    /**
     * 调阅人姓名
     */
    private String checkPersonName;

    /**
     * 调阅内容
     */
    private String consultContent;

    /**
     * 调阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;

    /**
     * 查询详情对象
     */
    private List<InspectionConsultInfoDetailPO> consultInfoDetailList;

    /**
     * 文件信息
     */
    private List<PiisDocumentPO> documents;
}
