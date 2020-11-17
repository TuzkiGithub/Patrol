package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.constant.ManagmentConstants;
import tech.piis.modules.managment.domain.RecommendBestPO;
import tech.piis.modules.managment.mapper.RecommendBestMapper;
import tech.piis.modules.managment.service.IRecommendBestService;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.mapper.SysDeptMapper;

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
public class RecommendBestServiceImpl implements IRecommendBestService {

    @Autowired
    private RecommendBestMapper recommendBestMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 新增择优推荐
     *
     * @param recommendBestPO
     * @return
     */
    @Override
    public int saveRecommendBest(RecommendBestPO recommendBestPO) {
        String parentId = recommendBestPO.getOrgId();
        if (!ManagmentConstants.FIRST_BRANCH_UNION_ID.contains(parentId)) {
            parentId = getParentId(recommendBestPO.getOrgId());
        }
        recommendBestPO.setFirstbranchId(parentId);
        return recommendBestMapper.insert(recommendBestPO);
    }

    private String getParentId(String orgId) {
        SysDept dept = sysDeptMapper.selectDeptById(orgId);
        String parentId = dept.getParentId();
        if (!ManagmentConstants.FIRST_BRANCH_UNION_ID.contains(parentId)) {
            parentId = getParentId(parentId);
        }
        return parentId;
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
     *
     * @param recommendBestPO
     * @return
     */
    @Override
    public List<RecommendBestPO> selectRecommendList(RecommendBestPO recommendBestPO) {
        QueryWrapper<RecommendBestPO> queryWrapper = new QueryWrapper<>();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("MEMBER_NAME", recommendBestPO.getMemberName());
        paramMap.put("MEMBER_UNIT", recommendBestPO.getMemberUnit());
        paramMap.put("RECOMMEND_YEAR", recommendBestPO.getRecommendYear());
        paramMap.put("RECOMMEND_TYPE", recommendBestPO.getRecommendType());
        queryWrapper.allEq(paramMap,false);
        return recommendBestMapper.selectList(queryWrapper);
    }

    /**
     * 择优推荐总览根据机构分组查询择优推荐次数
     *
     * @return
     */
    @Override
    public List<RecommendBestPO> selectRecommendListByOrgId() {

        return recommendBestMapper.selectRecommendListByOrgId();
    }

    /**
     * 修改择优推荐
     *
     * @param recommendBestPO
     * @return
     */
    @Override
    public int update(RecommendBestPO recommendBestPO) {
        return recommendBestMapper.updateById(recommendBestPO);
    }


}
