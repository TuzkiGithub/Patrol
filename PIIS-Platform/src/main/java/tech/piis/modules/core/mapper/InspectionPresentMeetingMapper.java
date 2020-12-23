package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionPresentMeetingPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 列席会议 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-07
 */
public interface InspectionPresentMeetingMapper extends BaseMapper<InspectionPresentMeetingPO> {
    /**
     * 统计巡视方案下被巡视单位列席会议 次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionPresentMeetingCount(String planId) throws BaseException;

    /**
    * 根据ID查询列席会议 以及文件信息
    * @return
    * @throws BaseException
    */
    InspectionPresentMeetingPO selectInspectionPresentMeetingWithFileById(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException;
}
