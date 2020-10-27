package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionImportantReportPO;
import java.util.List;

/**
 * 重要情况专题报告 Service接口
 *
 * @author Kevin
 * @date 2020-10-23
 */
public interface IInspectionImportantReportService {

    /**
     * 统计巡视方案下被巡视单位InspectionImportantReport次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionImportantReportCount(String planId) throws BaseException;

    /**
     * 查询重要情况专题报告 列表
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    List<InspectionImportantReportPO> selectInspectionImportantReportList(InspectionImportantReportPO inspectionImportantReport) throws BaseException;

    /**
    * 新增重要情况专题报告 
    * @param inspectionImportantReport
    * @return
    * @throws BaseException
    */
    int save(InspectionImportantReportPO inspectionImportantReport) throws BaseException;

    /**
     * 根据ID修改重要情况专题报告 
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    int update(InspectionImportantReportPO inspectionImportantReport) throws BaseException;

    /**
     * 根据ID批量删除重要情况专题报告 
     * @param inspectionImportantReportIds 重要情况专题报告 编号
     *
     * @return
     */
    int deleteByInspectionImportantReportIds(Long[] inspectionImportantReportIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
