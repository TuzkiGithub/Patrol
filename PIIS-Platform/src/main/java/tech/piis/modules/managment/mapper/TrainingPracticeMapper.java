package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.po.TrainingPracticePO;

import java.util.List;

/**
 * ClassName : TrainingPracticeMapper
 * Package : tech.piis.modules.managment.mapper
 * Description : 以干代训 mapper
 *
 * @author : chenhui@xvco.com
 */
public interface TrainingPracticeMapper extends BaseMapper<TrainingPracticePO> {
    /**
     * 获取以干代训列表
     * @param trainingPracticePO
     * @return
     */
    List<TrainingPracticePO> selectListByConditions(TrainingPracticePO trainingPracticePO);
    /**
     * 以干代训总览根据机构分组查询以干代训次数
     * @return
     */
    List<TrainingPracticePO> selectPracticeListByOrgId();
}
