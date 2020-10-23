package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 抽查个人事项报告对象 inspection_check_person_matters
 * 
 * @author Kevin
 * @date 2020-10-13
 */

@TableName("inspection_check_person_matters")
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionCheckPersonMattersPO extends PIBaseEntity
{
    /** 抽查个人报告编号 */
    @TableId(value = "CHECK_PERSON_MATTERS_ID", type = IdType.AUTO)
    private Long checkPersonMattersId;
    /** 计划ID */
    private String planId;
    /**
     * 被巡视单位ID
     */
    private Long unitsId;
    /** 被抽查人ID */
    private String checkerId;
    /** 被抽查人姓名 */
    private String checkerName;
    /** 报告名称 */
    private String checkName;
    /** 抽查时间 */

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;

    /**
     * 文件信息
     */
    @NotEmpty(message = "文件不能为空！")
    private List<PiisDocumentPO> documents;
}
