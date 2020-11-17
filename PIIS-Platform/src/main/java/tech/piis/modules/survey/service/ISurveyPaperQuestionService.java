package tech.piis.modules.survey.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyPaperQuestionPO;

import java.util.List;

/**
 * 试卷题目关系Service接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface ISurveyPaperQuestionService {

    /**
     * 查询试卷题目关系列表
     *
     * @param surveyPaperQuestion
     * @return
     * @throws BaseException
     */
    List<SurveyPaperQuestionPO> selectSurveyPaperQuestionList(SurveyPaperQuestionPO surveyPaperQuestion) throws BaseException;

    /**
     * 新增试卷题目关系
     *
     * @param surveyPaperQuestion
     * @return
     * @throws BaseException
     */
    int save(SurveyPaperQuestionPO surveyPaperQuestion) throws BaseException;

    /**
     * 根据ID修改试卷题目关系
     *
     * @param surveyPaperQuestion
     * @return
     * @throws BaseException
     */
    int update(SurveyPaperQuestionPO surveyPaperQuestion) throws BaseException;

    /**
     * 根据ID批量删除试卷题目关系
     *
     * @param paperQuestionIds 试卷题目关系编号
     * @return
     */
    int deleteBySurveyPaperQuestionIds(Long[] paperQuestionIds);

    /**
     * 统计总数
     *
     * @return
     */
    int count();
}
