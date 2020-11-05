package tech.piis.modules.managment.service;

/**
 * ClassName : OrganizationService
 * Package : tech.piis.modules.managment.service
 * Description :
 *  机构 service
 * @author : chenhui@xvco.com
 */

public interface OrganizationService {
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
}
