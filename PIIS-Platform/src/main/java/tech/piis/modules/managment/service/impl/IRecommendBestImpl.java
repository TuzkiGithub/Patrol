package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.managment.domain.RecommendBestPO;
import tech.piis.modules.managment.mapper.RecommendBestMapper;
import tech.piis.modules.managment.service.RecommendBestService;

import java.util.*;

/**
 * ClassName : IRecommendBestImpl
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 * 择优推荐 业务层处理
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class IRecommendBestImpl implements RecommendBestService {

    @Autowired
    private RecommendBestMapper recommendBestMapper;

    /**
     * 新增择优推荐
     *
     * @param recommendBestPO
     * @return
     */
    @Override
    public int saveRecommendBest(RecommendBestPO recommendBestPO) {
        return recommendBestMapper.insert(recommendBestPO);
    }

    /**
     * 删除择优推荐
     *
     * @param ids
     * @return
     */
    @Override
    public int delRecommendByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        return recommendBestMapper.deleteBatchIds(idList);
    }

    /**
     * 根据择优推荐ID查询择优推荐信息
     *
     * @param id
     * @return
     */
    @Override
    public RecommendBestPO getRecommendById(String id) {
        return recommendBestMapper.selectById(id);
    }

    /**
     * 获取择优推荐记录列表
     * @param recommendBestPO
     * @return
     */
    @Override
    public List<RecommendBestPO> selectRecommendList(RecommendBestPO recommendBestPO) {
        return recommendBestMapper.selectListByConditions(recommendBestPO);
    }

    @Override
    public long selectCount(RecommendBestPO recommendBestPO) {
        return recommendBestMapper.selectTotal(recommendBestPO);
    }


}
