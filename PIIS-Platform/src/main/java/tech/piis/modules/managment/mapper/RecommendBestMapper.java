package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.po.RecommendBestPO;

import java.util.List;

/**
 * ClassName : RecommendBestMapper
 * Package : tech.piis.modules.managment.mapper
 * Description : 择优推荐 mapper
 *
 * @author : chenhui@xvco.com
 */
public interface RecommendBestMapper extends BaseMapper<RecommendBestPO> {
    /**
     * 择优推荐总览根据机构分组查询择优推荐次数
     * @return
     */
    List<RecommendBestPO> selectRecommendListByOrgId();
}
