package tech.piis.modules.managment.service;

import tech.piis.modules.managment.domain.FullPartOrgPO;

import java.util.List;

/**
 * ClassName : FullPartOrgService
 * Package : tech.piis.modules.managment.service
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
     * @param fullPartOrgPO
     * @return
     */
    List<FullPartOrgPO> selectByWholeName(FullPartOrgPO fullPartOrgPO);
}
