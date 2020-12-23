package tech.piis.modules.person.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName : Organization
 * Package : tech.piis.modules.person.domain
 * Description :
 *  机构实体类
 * @author : chenhui@xvco.com
 */
@Data
@TableName("person_organization")
public class OrganizationPO extends MABaseEntity {

    /**
     * 机构编号
     */
    @TableId("id")
    private String orgId;
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
}
