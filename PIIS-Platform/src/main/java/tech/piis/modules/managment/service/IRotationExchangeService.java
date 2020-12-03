package tech.piis.modules.managment.service;


import tech.piis.modules.managment.domain.po.RotationExchangePO;

import java.util.List;

/**
 * ClassName : RotationExchangeService
 * Package : tech.piis.modules.managment.service
 * Description :
 *  轮岗交流Mapper
 * @author : chenhui@xvco.com
 */
public interface IRotationExchangeService {
    /**
     * 轮岗交流总览
     * @return
     */
    List<RotationExchangePO> selectRecommendListByOrgId();

    /**
     * 根据条件动态查询轮岗交流记录
     * @param rotationExchangePO
     * @return
     */
    List<RotationExchangePO> selectRotationList(RotationExchangePO rotationExchangePO);

    /**
     * 新增轮岗交流记录
     * @param rotationExchangePO
     * @return
     */
    int saveRotationExchange(RotationExchangePO rotationExchangePO);

    /**
     * 删除轮岗交流记录
     * @param ids
     * @return
     */
    int delRotationByIds(String[] ids);

    /**
     * 修改轮岗交流
     * @param rotationExchangePO
     * @return
     */
    int update(RotationExchangePO rotationExchangePO);
}
