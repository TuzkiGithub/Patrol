package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionLearnTrainPO;
import java.util.List;

/**
 * 学习培训 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionLearnTrainService {

    /**
     * 查询学习培训 列表
     * @param inspectionLearnTrain
     * @return
     * @throws BaseException
     */
    List<InspectionLearnTrainPO> selectInspectionLearnTrainList(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException;

    /**
    * 新增学习培训 
    * @param inspectionLearnTrain
    * @return
    * @throws BaseException
    */
    int save(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException;

    /**
     * 根据ID修改学习培训 
     * @param inspectionLearnTrain
     * @return
     * @throws BaseException
     */
    int update(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException;

    /**
     * 根据ID批量删除学习培训 
     * @param learnTrainIds 学习培训 编号
     *
     * @return
     */
    int deleteByInspectionLearnTrainIds(Long[] learnTrainIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
