package tech.piis.modules.managment.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * ClassName : InspectionOfficePO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *  巡视/巡察办成立表
 * @author : chenhui@xvco.com
 */
@Data
@TableName("inspection_office")
public class InspectionOfficePO extends MABaseEntity{
    /**
     * 巡视/巡察办成立编号
     */
    @TableId(value = "INSPECTION_OFFICE_ID")
    private String inspectionOfficeId;
    /**
     * 巡视/巡察办所属机构编号
     */
    @NotBlank(message = "所属机构编号不能为空")
    private String orgId;
    /**
     * 成立时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @NotNull(message = "成立日期不能为空")
//    private Date foundTime;

    @TableField(exist = false)
    @NotEmpty(message = "成立文件不能为空")
    private List<PiisDocumentPO> officeFoundDocList;

}
