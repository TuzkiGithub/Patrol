package tech.piis.modules.survey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyQuestionPO;

import java.util.List;

/**
 * 题目Mapper接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface SurveyQuestionMapper extends BaseMapper<SurveyQuestionPO> {

    /**
     * 查询题目列表
     *
     * @return
     * @throws BaseException
     */
    List<SurveyQuestionPO> selectSurveyQuestionList(SurveyQuestionPO question) throws BaseException;

    /**
     * 批量新增题目
     * @param questionList
     * @return
     * @throws BaseException
     */
    int insertQuestionBatch(List<SurveyQuestionPO> questionList) throws BaseException;

    /**
     * 随机查询题目
     * @return
     * @throws BaseException
     */
    List<SurveyQuestionPO> selectQuestionRandom() throws BaseException;


}
