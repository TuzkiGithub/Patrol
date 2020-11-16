package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingPO;
import java.util.List;

/**
 * 下沉了解Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionSinkingUnderstandingService {

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstanding次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionSinkingUnderstandingCount(String planId) throws BaseException;

    /**
     * 查询下沉了解列表
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    List<InspectionSinkingUnderstandingPO> selectInspectionSinkingUnderstandingList(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException;

    /**
    * 新增下沉了解
    * @param inspectionSinkingUnderstanding
    * @return
    * @throws BaseException
    */
    int save(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException;

    /**
     * 根据ID修改下沉了解
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    int update(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException;

    /**
     * 根据ID批量删除下沉了解
     * @param sinkingUnderstandingIds 下沉了解编号
     *
     * @return
     */
    int deleteByInspectionSinkingUnderstandingIds(String[] sinkingUnderstandingIds);

    /**
     * 查询总数
     * @return
     */
    int count(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding);
}
