package tech.piis.modules.managment.service;

import tech.piis.modules.managment.domain.DailyTrainingPO;

import java.util.List;

/**
 * ClassName : DailyTrainingService
 * Package : tech.piis.modules.managment.service
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface IDailyTrainingService {
    /**
     * 新增日常培训
     * @param dailyTrainingPO
     * @return
     */
    int saveDailyTraining(DailyTrainingPO dailyTrainingPO);

    /**
     * 修改日常培训
     * @param dailyTrainingPO
     * @return
     */
    int updateDailyTraining(DailyTrainingPO dailyTrainingPO);

    /**
     * 删除日常培训
     * @param id
     * @return
     */
    int delDailyTrainingById(String id);

    /**
     * 查询日常培训列表
     * @param dailyTrainingPO
     * @return
     */
    List<DailyTrainingPO> selectDailyTrainingList(DailyTrainingPO dailyTrainingPO);

    /**
     * 查询日常培训信息及其关联培训人员与培训课程信息
     * @param dailyId
     * @return
     */
    DailyTrainingPO selectDailyTrainingInfo(String dailyId);

    /**
     * 根据所属一级机构编号查询各个机构日常培训次数
     * @param
     * @return
     */
    List<DailyTrainingPO> selectDailyTrainingByOrgId();

    /**
     * 查询日常培训列表
     * @param dailyTrainingPO
     * @return
     */
  //  List<DailyTrainingPO> selectDailyTrainingListByConditions(DailyTrainingPO dailyTrainingPO);
}
