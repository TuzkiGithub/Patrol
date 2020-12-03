package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionVisitPO;
import java.util.List;

/**
 * 来访Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionVisitService {

    /**
     * 统计巡视方案下被巡视单位InspectionVisit次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionVisitCount(String planId) throws BaseException;

    /**
     * 查询来访列表
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    List<InspectionVisitPO> selectInspectionVisitList(InspectionVisitPO inspectionVisit) throws BaseException;

    /**
    * 新增来访
    * @param inspectionVisit
    * @return
    * @throws BaseException
    */
    int save(InspectionVisitPO inspectionVisit) throws BaseException;

    /**
     * 根据ID修改来访
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    int update(InspectionVisitPO inspectionVisit) throws BaseException;

    /**
     * 根据ID批量删除来访
     * @param callVisitIds 来访编号
     *
     * @return
     */
    int deleteByInspectionVisitIds(String[] callVisitIds);

    /**
     * 审批
     * @param visitList
     */
    void doApprovals(List<InspectionVisitPO> visitList);
}
