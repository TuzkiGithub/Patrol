package tech.piis.modules.managment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.constant.ManagmentConstants;
import tech.piis.modules.managment.domain.RotationExchangePO;
import tech.piis.modules.managment.mapper.RotationExchangeMapper;
import tech.piis.modules.managment.service.IRotationExchangeService;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.mapper.SysDeptMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName : IRotationExchangeServiceImpl
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 * 轮岗交流业务处理层
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class RotationExchangeServiceImpl implements IRotationExchangeService {
    @Autowired
    private RotationExchangeMapper rotationExchangeMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 轮岗交流次数
     *
     * @return
     */
    @Override
    public List<RotationExchangePO> selectRecommendListByOrgId() {
        return rotationExchangeMapper.selectRecommendListByOrgId();
    }

    /**
     * 根据条件动态查询轮岗交流记录
     *
     * @param rotationExchangePO
     * @return
     */
    @Override
    public List<RotationExchangePO> selectRotationList(RotationExchangePO rotationExchangePO) {
        QueryWrapper<RotationExchangePO> queryWrapper = new QueryWrapper<>();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("member_name", rotationExchangePO.getMemberName());
        paramMap.put("member_unit", rotationExchangePO.getMemberUnit());
        queryWrapper.allEq(paramMap,false);
        return rotationExchangeMapper.selectList(queryWrapper);
    }

    /**
     * 新增轮岗交流
     *
     * @param rotationExchangePO
     * @return
     */
    @Override
    public int saveRotationExchange(RotationExchangePO rotationExchangePO) {
        String parentId = rotationExchangePO.getOrgId();
        if (!ManagmentConstants.FIRST_BRANCH_UNION_ID.contains(parentId)) {
            parentId = getParentId(rotationExchangePO.getOrgId());
        }
        rotationExchangePO.setFirstbranchId(parentId);
        return rotationExchangeMapper.insert(rotationExchangePO);

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
     * 删除轮岗交流
     *
     * @param ids
     * @return
     */
    @Override
    public int delRotationByIds(String[] ids) {
        List<String> list = Arrays.asList(ids);
        return rotationExchangeMapper.deleteBatchIds(list);
    }

    /**
     * 修改轮岗交流
     *
     * @param rotationExchangePO
     * @return
     */
    @Override
    public int update(RotationExchangePO rotationExchangePO) {
        return rotationExchangeMapper.updateById(rotationExchangePO);
    }


}
