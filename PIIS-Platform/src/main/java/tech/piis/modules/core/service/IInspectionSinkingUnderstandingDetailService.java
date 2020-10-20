package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingDetailPO;
import java.util.List;

/**
 * 下沉了解详情Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionSinkingUnderstandingDetailService {

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstandingDetail次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionSinkingUnderstandingDetailCount(String planId) throws BaseException;

    /**
     * 查询下沉了解详情列表
     * @param inspectionSinkingUnderstandingDetail
     * @return
     * @throws BaseException
     */
    List<InspectionSinkingUnderstandingDetailPO> selectInspectionSinkingUnderstandingDetailList(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException;

    /**
    * 新增下沉了解详情
    * @param inspectionSinkingUnderstandingDetail
    * @return
    * @throws BaseException
    */
    int save(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException;

    /**
     * 根据ID修改下沉了解详情
     * @param inspectionSinkingUnderstandingDetail
     * @return
     * @throws BaseException
     */
    int update(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException;

    /**
     * 根据ID批量删除下沉了解详情
     * @param sinkngUnderstandingDetailIds 下沉了解详情编号
     *
     * @return
     */
    int deleteByInspectionSinkingUnderstandingDetailIds(Long[] sinkngUnderstandingDetailIds);
}
