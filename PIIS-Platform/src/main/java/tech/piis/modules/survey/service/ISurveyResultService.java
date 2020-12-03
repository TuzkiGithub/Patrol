package tech.piis.modules.survey.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyResultPO;

import java.util.List;

/**
 * 试卷/问卷回答情况Service接口
 *
 * @author Tuzki
 * @date 2020-11-18
 */
public interface ISurveyResultService {

    /**
     * 查询试卷/问卷回答情况列表
     * @param surveyResult
     * @return
     * @throws BaseException
     */
    List<SurveyResultPO> selectSurveyResultList(SurveyResultPO surveyResult) throws BaseException;

    /**
    * 新增试卷/问卷回答情况
    * @param surveyResult
    * @return
    * @throws BaseException
    */
    int save(SurveyResultPO surveyResult) throws BaseException;

    /**
     * 根据ID修改试卷/问卷回答情况
     * @param surveyResult
     * @return
     * @throws BaseException
     */
    int update(SurveyResultPO surveyResult) throws BaseException;

    /**
     * 根据ID批量删除试卷/问卷回答情况
     * @param resultIds 试卷/问卷回答情况编号
     *
     * @return
     */
    int deleteBySurveyResultIds(Long[] resultIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}