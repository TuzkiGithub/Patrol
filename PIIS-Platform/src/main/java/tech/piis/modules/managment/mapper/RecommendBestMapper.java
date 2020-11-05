package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.RecommendBestPO;

import java.util.List;
import java.util.Map;

/**
 * ClassName : RecommendBestMapper
 * Package : tech.piis.modules.managment.mapper
 * Description : 择优推荐 mapper
 *
 * @author : chenhui@xvco.com
 */
public interface RecommendBestMapper extends BaseMapper<RecommendBestPO> {
    /**
     * 获取择优推荐列表
     * @param recommendBestPO
     * @return
     */
    List<RecommendBestPO> selectListByConditions(RecommendBestPO recommendBestPO);

    /**
     * 获取总记录条数
     * @param recommendBestPO
     * @return
     */
    long selectTotal(RecommendBestPO recommendBestPO);
}
