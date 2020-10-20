package tech.piis.modules.core.service;


import tech.piis.modules.core.domain.po.InspectionSpecialReportPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

import java.util.List;

/**
 * SPECIAL_REPORT Service接口
 *
 * @author Kevin
 * @date 2020-10-12
 */
public interface IInspectionSpecialReportService {

    /**
     * 统计巡视方案下被巡视单位的听取报告次数
     *
     * @param planId
     * @return
     */
    List<UnitsBizCountVO> selectSpecialReportCount(String planId);

    List<InspectionSpecialReportPO> selectSpecialReport(InspectionSpecialReportPO inspectionSpecialReport);

    int deleteBySpecialReportIds(Long[] specialReportIds);

    int update(InspectionSpecialReportPO inspectionSpecialReport) throws Exception;

    int save(InspectionSpecialReportPO inspectionSpecialReport) throws Exception;

}
