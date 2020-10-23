package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPO;
import java.util.List;

/**
 * 调研走访Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionInvestigationVisitService {

    /**
     * 统计巡视方案下被巡视单位InspectionInvestigationVisit次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionInvestigationVisitCount(String planId) throws BaseException;

    /**
     * 查询调研走访列表
     * @param inspectionInvestigationVisit
     * @return
     * @throws BaseException
     */
    List<InspectionInvestigationVisitPO> selectInspectionInvestigationVisitList(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException;

    /**
    * 新增调研走访
    * @param inspectionInvestigationVisit
    * @return
    * @throws BaseException
    */
    int save(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException;

    /**
     * 根据ID修改调研走访
     * @param inspectionInvestigationVisit
     * @return
     * @throws BaseException
     */
    int update(InspectionInvestigationVisitPO inspectionInvestigationVisit) throws BaseException;

    /**
     * 根据ID批量删除调研走访
     * @param investigationVisitIds 调研走访编号
     *
     * @return
     */
    int deleteByInspectionInvestigationVisitIds(String[] investigationVisitIds);

    /**
     * 查询总数
     * @return
     */
    int count() throws BaseException;
}
