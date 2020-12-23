package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionPatrolReportPO;
import java.util.List;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

/**
 * 巡视报告Service接口
 *
 * @author Tuzki
 * @date 2020-12-11
 */
public interface IInspectionPatrolReportService {

    /**
     * 统计巡视方案下被巡视单位InspectionPatrolReport次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionPatrolReportCount(String planId) throws BaseException;

    /**
     * 查询巡视报告列表
     * @param inspectionPatrolReport
     * @return
     * @throws BaseException
     */
    List<InspectionPatrolReportPO> selectInspectionPatrolReportList(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException;

    /**
    * 新增巡视报告
    * @param inspectionPatrolReport
    * @return
    * @throws BaseException
    */
    int save(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException;

    /**
     * 根据ID修改巡视报告
     * @param inspectionPatrolReport
     * @return
     * @throws BaseException
     */
    int update(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException;

    /**
     * 根据ID批量删除巡视报告
     * @param patrolReportIds 巡视报告编号
     *
     * @return
     */
    int deleteByInspectionPatrolReportIds(Long[] patrolReportIds) throws BaseException;



    /**
    * 批量审批
    * @throws BaseException
    */
    void doApprovals(List<InspectionPatrolReportPO> inspectionPatrolReportList) throws BaseException;
}
