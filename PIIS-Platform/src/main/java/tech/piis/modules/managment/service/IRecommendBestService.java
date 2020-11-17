package tech.piis.modules.managment.service;

import tech.piis.modules.managment.domain.RecommendBestPO;

import java.util.List;

/**
 * ClassName : RecommendBestService
 * Package : tech.piis.modules.managment.service
 * Description : 择优推荐 service
 *
 * @author : chenhui@xvco.com
 */
public interface IRecommendBestService {
    /**
     * 新增择优推荐
     * @param recommendBestPO
     * @return
     */
    int saveRecommendBest(RecommendBestPO recommendBestPO);

    /**
     * 删除择优推荐
     * @param ids
     * @return
     */
    int delRecommendByIds(String[] ids);

    /**
     * 根据ID查询单条择优推荐记录
     * @param id
     * @return
     */
    RecommendBestPO getRecommendById(String id);

    /**
     * 根据条件动态查询择优推荐
     * @param recommendBestPO
     * @return
     */
    List<RecommendBestPO> selectRecommendList(RecommendBestPO recommendBestPO);

    /**
     * 择优推荐总览根据机构分组查询择优推荐次数
     * @return
     */
    List<RecommendBestPO> selectRecommendListByOrgId();

    /**
     * 修改择优推荐
     * @param recommendBestPO
     * @return
     */
    int update(RecommendBestPO recommendBestPO);
}
