package tech.piis.modules.managment.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName : FullPartOrg
 * Package : tech.piis.modules.managment.domain
 * Description :
 *  专兼职管理实体类
 * @author : chenhui@xvco.com
 */
@Data
@Accessors(chain = true)
public class FullPartOrg implements Serializable {
    /**
     * 编号
     */
    private String id;
    /**
     * 机构编号
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     *  是否拥有领导小组 0-->有 1-->无
     */
    private String isLeading;
    /**
     * 巡察办设立模式 0--> 单独设立 1--> 合署办公
     */
    private String officeMode;
    /**
     * 专职人数
     */
    private Integer fullNumber;
    /**
     * 兼职人数
     */
    private Integer partNumber;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 租户编号
     */
    private String entId;
}
