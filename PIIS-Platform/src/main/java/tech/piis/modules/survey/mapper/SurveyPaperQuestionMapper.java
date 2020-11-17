package tech.piis.modules.survey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyPaperQuestionPO;

import java.util.List;

/**
 * 试卷题目关系Mapper接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface SurveyPaperQuestionMapper extends BaseMapper<SurveyPaperQuestionPO> {

    int insertPaperQuestionBatch(List<SurveyPaperQuestionPO> surveyPaperQuestionPOS) throws BaseException;
}
