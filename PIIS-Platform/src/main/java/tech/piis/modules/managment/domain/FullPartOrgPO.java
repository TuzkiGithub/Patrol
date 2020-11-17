package tech.piis.modules.managment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ClassName : FullPartOrg
 * Package : tech.piis.modules.managment.domain
 * Description :
 *  专兼职管理实体类
 * @author : chenhui@xvco.com
 */
@Data
@Accessors(chain = true)
@TableName("full_part_org")
public class FullPartOrgPO extends MABaseEntity {
    /**
     * 专兼职管理编号
     */
    @TableId(value = "full_id",type = IdType.AUTO)
    private Long fullId;
    /**
     * 机构编号
     */
    @NotEmpty(message = "机构编号不能为空")
    private String orgId;
    /**
     * 机构名称
     */
    @NotEmpty(message = "机构名称不能为空")
    private String orgName;
    /**
     *  是否拥有领导小组 0-->有 1-->无
     */
    @NotEmpty(message = "领导小组拥有信息不能为空")
    private String isLeading;
    /**
     * 巡察办设立模式 0--> 单独设立 1--> 合署办公
     */
    @NotEmpty(message = "设立模式不能为空")
    private String officeMode;
    /**
     * 专职人数
     */
    @NotNull(message = "专职人数不能为空")
    private Integer fullNumber;
    /**
     * 兼职人数
     */
    @NotNull(message = "兼职人数不能为空")
    private Integer partNumber;
    /**
     * 巡察要点
     */
    @NotEmpty(message = "巡察要点不能为空")
    private String inspectionPoint;
    /**
     * 备注
     */
    private String remark;
    /**
     * 巡察制度文件编号
     */
    @NotNull(message = "巡察制度文件编号不能为空")
    private Long piisDocId;
    /**
     * 巡察制度文件名称
     */
    @TableField(exist = false)
    private String fileName;
}
