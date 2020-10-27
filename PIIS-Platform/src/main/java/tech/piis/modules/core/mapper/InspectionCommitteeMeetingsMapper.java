package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionCommitteeMeetingsPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 党委会小组会纪要 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-10-27
 */
public interface InspectionCommitteeMeetingsMapper extends BaseMapper<InspectionCommitteeMeetingsPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionCommitteeMeetings次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionCommitteeMeetingsCount(String planId) throws BaseException;
}
