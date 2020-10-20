package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionOrganizationMeetingsPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 组织会议Mapper接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionOrganizationMeetingsMapper extends BaseMapper<InspectionOrganizationMeetingsPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionOrganizationMeetings次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionOrganizationMeetingsCount(String planId) throws BaseException;
}