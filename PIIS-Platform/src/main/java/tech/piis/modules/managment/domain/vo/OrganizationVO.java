package tech.piis.modules.managment.domain.vo;

import lombok.Data;

/**
 * ClassName : OrganizationVO
 * Package : tech.piis.modules.managment.domain.vo
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
public class OrganizationVO {
    /**
     * 机构编号
     */
    private String orgId;
    /**
     * 巡察巡视机构全称
     */
    private String piisWholeName;
    private String orgName;
    private Integer fullNumber;
    private Integer partNumber;
    private Integer sonInspectionOrgNumber;
    private Integer sonInspectionPeopleNumber;
}
