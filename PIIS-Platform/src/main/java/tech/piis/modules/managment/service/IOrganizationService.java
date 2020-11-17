package tech.piis.modules.managment.service;

import java.util.Map;

/**
 * ClassName : OrganizationService
 * Package : tech.piis.modules.managment.service
 * Description :
 *  机构 service
 * @author : chenhui@xvco.com
 */

public interface IOrganizationService {
    /**
     *  获取机构表中巡察机构全称
     * @param organId
     * @return
     */
    String getInspectionOrganWholeName(String organId);

    /**
     * 获取 机构管理概览页面全部数据
     * @param organId
     * @return
     */
    Object getPageInfo(String organId);

    /**
     * 查询人员基础情况
     * @param memberId
     * @return
     */
    Map<String,Object> selectBasicInfoByMemberId(String memberId);
}
