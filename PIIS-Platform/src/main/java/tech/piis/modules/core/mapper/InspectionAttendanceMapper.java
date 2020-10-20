package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionAttendancePO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 参会情况Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionAttendanceMapper extends BaseMapper<InspectionAttendancePO> {

    /**
     * 统计巡视方案下被巡视单位InspectionAttendance次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionAttendanceCount(String planId) throws BaseException;
}
