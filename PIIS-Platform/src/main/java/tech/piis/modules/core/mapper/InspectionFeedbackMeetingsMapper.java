package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionFeedbackMeetingsPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 反馈会Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-11
 */
public interface InspectionFeedbackMeetingsMapper extends BaseMapper<InspectionFeedbackMeetingsPO> {
    /**
     * 统计巡视方案下被巡视单位反馈会次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionFeedbackMeetingsCount(String planId) throws BaseException;

    /**
    * 根据ID查询反馈会以及文件信息
    * @return
    * @throws BaseException
    */
    InspectionFeedbackMeetingsPO selectInspectionFeedbackMeetingsWithFileById(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException;
}
