package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionCallVisitPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

import java.util.List;

/**
 * 来电Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionCallVisitService {

    /**
     * 统计巡视方案下被巡视单位InspectionCallVisit次数
     *
     * @param planId 巡视计划ID
     */
    List<UnitsBizCountVO> selectInspectionCallVisitCount(String planId) throws BaseException;

    /**
     * 查询来电列表
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    List<InspectionCallVisitPO> selectInspectionCallVisitList(InspectionCallVisitPO inspectionCallVisit) throws BaseException;

    /**
     * 新增来电
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    int save(InspectionCallVisitPO inspectionCallVisit) throws BaseException;

    /**
     * 根据ID修改来电
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    int update(InspectionCallVisitPO inspectionCallVisit) throws BaseException;

    /**
     * 根据ID批量删除来电
     *
     * @param callVisitIds 来电编号
     * @return
     */
    int deleteByInspectionCallVisitIds(String[] callVisitIds);

    /**
     * 审批
     *
     * @param callVisitList
     */
    void doApprovals(List<InspectionCallVisitPO> callVisitList);
}
