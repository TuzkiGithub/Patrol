package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionFeedbackQuestionsPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 反馈问题清单 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-10-27
 */
public interface InspectionFeedbackQuestionsMapper extends BaseMapper<InspectionFeedbackQuestionsPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionFeedbackQuestions次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionFeedbackQuestionsCount(String planId) throws BaseException;
}
