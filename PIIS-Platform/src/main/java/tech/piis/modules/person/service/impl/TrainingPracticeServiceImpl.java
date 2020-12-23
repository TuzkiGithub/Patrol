package tech.piis.modules.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.constant.ManagmentConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.person.domain.po.TrainingPracticePO;
import tech.piis.modules.person.mapper.TrainingPracticeMapper;
import tech.piis.modules.person.service.ITrainingPracticeService;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.mapper.SysDeptMapper;

import java.util.Arrays;
import java.util.List;

/**
 * ClassName : ITrainingPracticeServiceImpl
 * Package : tech.piis.modules.person.service.impl
 * Description : 以干代训 业务处理层
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class TrainingPracticeServiceImpl implements ITrainingPracticeService {

    @Autowired
    private TrainingPracticeMapper trainingPracticeMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 新增以干代训
     *
     * @param trainingPracticePO
     * @return
     */
    @Override
    public int saveRecommendBest(TrainingPracticePO trainingPracticePO) throws BaseException {
        String parentId = trainingPracticePO.getOrgId();
        if (!ManagmentConstants.FIRST_BRANCH_UNION_ID.contains(parentId)) {
            parentId = getParentId(trainingPracticePO.getOrgId());
        }
        trainingPracticePO.setFirstbranchId(parentId);
        return trainingPracticeMapper.insert(trainingPracticePO);
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
     * 删除以干代训
     *
     * @param ids
     * @return
     */
    @Override
    public int delRecommendByIds(String[] ids) throws BaseException {
        List<String> idList = Arrays.asList(ids);
        return trainingPracticeMapper.deleteBatchIds(idList);
    }

    /**
     * 根据以干代训ID查询以干代训信息
     *
     * @param id
     * @return
     */
    @Override
    public TrainingPracticePO getRecommendById(String id) throws BaseException {
        return trainingPracticeMapper.selectById(id);
    }

    /**
     * 获取以干代训记录列表
     *
     * @param trainingPracticePO
     * @return
     */
    @Override
    public List<TrainingPracticePO> selectPracticeList(TrainingPracticePO trainingPracticePO) throws BaseException {
        QueryWrapper<TrainingPracticePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(trainingPracticePO.getMemberName()), "member_name", trainingPracticePO.getMemberName());
        queryWrapper.like(StringUtils.isNotBlank(trainingPracticePO.getMemberUnit()), "member_unit", trainingPracticePO.getMemberUnit());
        queryWrapper.like(StringUtils.isNotBlank(trainingPracticePO.getTrainingType()), "training_type", trainingPracticePO.getTrainingType());
        queryWrapper.orderByDesc("created_time");
        return trainingPracticeMapper.selectList(queryWrapper);
    }

    /**
     * 以干代训总览根据机构分组查询以干代训次数
     *
     * @return
     */
    @Override
    public List<TrainingPracticePO> selectPracticeListByOrgId() throws BaseException {
        return trainingPracticeMapper.selectPracticeListByOrgId();
    }

    /**
     * 修改以干代训
     *
     * @param trainingPracticePO
     * @return
     */
    @Override
    public int update(TrainingPracticePO trainingPracticePO) throws BaseException {
        return trainingPracticeMapper.updateById(trainingPracticePO);
    }


}
