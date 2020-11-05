package tech.piis.modules.managment.service;

import tech.piis.modules.managment.domain.RecommendBestPO;

import java.util.List;
import java.util.Map;

/**
 * ClassName : RecommendBestService
 * Package : tech.piis.modules.managment.service
 * Description : 择优推荐 service
 *
 * @author : chenhui@xvco.com
 */
public interface RecommendBestService {
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
    List<?> selectRecommendList(RecommendBestPO recommendBestPO);

    /**
     * 查询总记录条数
     * @param recommendBestPO
     * @return
     */
    long selectCount(RecommendBestPO recommendBestPO);
}
