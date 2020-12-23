package tech.piis.modules.person.service;

import tech.piis.modules.person.domain.po.FullPartOrgPO;

import java.util.List;

/**
 * ClassName : FullPartOrgService
 * Package : tech.piis.modules.person.service
 * Description :
 *  专兼职管理 service
 * @author : chenhui@xvco.com
 */
public interface IFullPartOrgService {
    /**
     * 新增专兼职管理
     * @param fullPartOrgPO
     * @return
     */
    int save(FullPartOrgPO fullPartOrgPO);

    /**
     * 修改专兼职管理
     * @param fullPartOrgPO
     * @return
     */
    int update(FullPartOrgPO fullPartOrgPO);

    /**
     * 删除专兼职管理
     * @param fullPartOrgId
     * @return
     */
    int deleteByFullPartOrgId(String[] fullPartOrgId);

    /**
     * 查询专兼职管理
     * @param OrgName
     * @return
     */
    List<FullPartOrgPO> selectByWholeName(String OrgName);
}
