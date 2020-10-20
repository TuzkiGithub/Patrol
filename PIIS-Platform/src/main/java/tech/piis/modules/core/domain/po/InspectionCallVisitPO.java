package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 来电对象 inspection_call_visit
 *
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_call_visit")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionCallVisitPO extends PIBaseEntity {
    /**
     * 来电编号
     */
    @TableId(value = "CALL_VISIT_ID", type = IdType.AUTO)
    private Long callVisitId;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private String unitsId;
    /**
     * 来电时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date callTime;
    /**
     * 来电公司编号
     */
    private String callCompanyId;
    /**
     * 来电公司名称
     */
    private String callCompanyName;
    /**
     * 来电人编号
     */
    private String callPersonId;
    /**
     * 来电人姓名
     */
    private String callPersonName;
    /**
     * 来电号码
     */
    private String callNumber;
    /**
     * 记录人编号
     */
    private String recorderId;
    /**
     * 记录人名称
     */
    private String recorderName;
    /**
     * 记录人公司编号
     */
    private String recorderCompanyId;
    /**
     * 记录人公司名称
     */
    private String recorderCompanyName;
    /**
     * 通话内容
     */
    private String callContent;


    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
