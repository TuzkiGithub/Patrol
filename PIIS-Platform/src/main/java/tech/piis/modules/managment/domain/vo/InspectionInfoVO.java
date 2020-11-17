package tech.piis.modules.managment.domain.vo;

import lombok.Data;

/**
 * ClassName : InspectionInfoVO
 * Package : tech.piis.modules.managment.domain.vo
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
public class InspectionInfoVO {
    /**
     * 所属巡察机构名称
     */
    private String orgName;
    /**
     * 所属巡查机构编号
     */
    private String orgId;
    /**
     * 所属巡察机构人员数量
     */
    private Integer memberNum;
}
