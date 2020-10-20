package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionConsultInfoDetailPO;
import java.util.List;

/**
 * 查阅资料详情Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionConsultInfoDetailService {

    /**
     * 统计巡视方案下被巡视单位InspectionConsultInfoDetail次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionConsultInfoDetailCount(String planId) throws BaseException;

    /**
     * 查询查阅资料详情列表
     * @param inspectionConsultInfoDetail
     * @return
     * @throws BaseException
     */
    List<InspectionConsultInfoDetailPO> selectInspectionConsultInfoDetailList(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException;

    /**
    * 新增查阅资料详情
    * @param inspectionConsultInfoDetail
    * @return
    * @throws BaseException
    */
    int save(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException;

    /**
     * 根据ID修改查阅资料详情
     * @param inspectionConsultInfoDetail
     * @return
     * @throws BaseException
     */
    int update(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException;

    /**
     * 根据ID批量删除查阅资料详情
     * @param consultInfoDetailIds 查阅资料详情编号
     *
     * @return
     */
    int deleteByInspectionConsultInfoDetailIds(Long[] consultInfoDetailIds);
}
