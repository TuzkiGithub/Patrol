package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 反馈意见 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-10-27
 */
public interface InspectionFeedbackMapper extends BaseMapper<InspectionFeedbackPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionFeedback次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionFeedbackCount(String planId) throws BaseException;
}
