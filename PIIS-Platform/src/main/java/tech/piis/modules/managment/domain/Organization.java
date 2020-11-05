package tech.piis.modules.managment.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName : Organization
 * Package : tech.piis.modules.managment.domain
 * Description :
 *  机构实体类
 * @author : chenhui@xvco.com
 */
@Data
@Accessors(chain = true)
public class Organization implements Serializable {

    /**
     * 机构编号
     */
    private String id;
    /**
     *  机构父级编号
     */
    private String pid;
    /**
     * 机构编码
     */
    private String code;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     *  机构所属巡察/巡视全称
     */
    private String piisWholeName;
    /**
     *  创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     *  修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 租户
     */
    private String entId;
}
