package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionAttendancePO;
import java.util.List;

/**
 * 参会情况Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionAttendanceService {

    /**
     * 统计巡视方案下被巡视单位InspectionAttendance次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionAttendanceCount(String planId) throws BaseException;

    /**
     * 查询参会情况列表
     * @param inspectionAttendance
     * @return
     * @throws BaseException
     */
    List<InspectionAttendancePO> selectInspectionAttendanceList(InspectionAttendancePO inspectionAttendance) throws BaseException;

    /**
    * 新增参会情况
    * @param inspectionAttendance
    * @return
    * @throws BaseException
    */
    int save(InspectionAttendancePO inspectionAttendance) throws BaseException;

    /**
     * 根据ID修改参会情况
     * @param inspectionAttendance
     * @return
     * @throws BaseException
     */
    int update(InspectionAttendancePO inspectionAttendance) throws BaseException;

    /**
     * 根据ID批量删除参会情况
     * @param attendanceIds 参会情况编号
     *
     * @return
     */
    int deleteByInspectionAttendanceIds(Long[] attendanceIds);
}
