package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionMeetingsResearchPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 会议研究Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-11
 */
public interface InspectionMeetingsResearchMapper extends BaseMapper<InspectionMeetingsResearchPO> {
    /**
     * 统计巡视方案下被巡视单位会议研究次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionMeetingsResearchCount(String planId) throws BaseException;

    /**
    * 根据ID查询会议研究以及文件信息
    * @return
    * @throws BaseException
    */
    InspectionMeetingsResearchPO selectInspectionMeetingsResearchWithFileById(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException;
}
