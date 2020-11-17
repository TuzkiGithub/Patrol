package tech.piis.modules.managment.service;


import tech.piis.modules.managment.domain.TrainingPracticePO;

import java.util.List;

/**
 * ClassName : TrainingPracticeService
 * Package : tech.piis.modules.managment.service
 * Description : 以干代训 service
 *
 * @author : chenhui@xvco.com
 */
public interface ITrainingPracticeService {
    /**
     * 新增以干代训
     * @param trainingPracticePO
     * @return
     */
    int saveRecommendBest(TrainingPracticePO trainingPracticePO);

    /**
     * 删除以干代训
     * @param ids
     * @return
     */
    int delRecommendByIds(String[] ids);

    /**
     * 根据ID查询单条以干代训记录
     * @param id
     * @return
     */
    TrainingPracticePO getRecommendById(String id);

    /**
     * 根据条件动态查询以干代训
     * @param trainingPracticePO
     * @return
     */
    List<TrainingPracticePO> selectPracticeList(TrainingPracticePO trainingPracticePO);

    /**
     * 以干代训总览根据机构分组查询以干代训次数
     * @return
     */
    List<TrainingPracticePO> selectPracticeListByOrgId();

    /**
     * 修改以干代训
     * @param trainingPracticePO
     * @return
     */
    int update(TrainingPracticePO trainingPracticePO);
}
