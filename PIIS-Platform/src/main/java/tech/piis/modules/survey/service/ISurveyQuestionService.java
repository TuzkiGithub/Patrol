package tech.piis.modules.survey.service;

import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.exception.BaseException;
import tech.piis.common.exception.file.QuestionFileException;
import tech.piis.modules.survey.domain.po.SurveyQuestionPO;

import java.util.List;

/**
 * 题目Service接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface ISurveyQuestionService {

    /**
     * 查询题目列表
     *
     * @param surveyQuestion
     * @return
     * @throws BaseException
     */
    List<SurveyQuestionPO> selectSurveyQuestionList(SurveyQuestionPO surveyQuestion) throws BaseException;

    /**
     * 新增题目
     *
     * @param surveyQuestion
     * @return
     * @throws BaseException
     */
    int save(SurveyQuestionPO surveyQuestion) throws BaseException;

    /**
     * 根据ID修改题目
     *
     * @param surveyQuestion
     * @return
     * @throws BaseException
     */
    int update(SurveyQuestionPO surveyQuestion) throws BaseException;

    /**
     * 根据ID批量删除题目
     *
     * @param questionIds 题目编号
     * @return
     */
    int deleteBySurveyQuestionIds(String[] questionIds);

    /**
     * 通过模板导入题目
     * @param file
     * @param type 1：测评 2：问卷
     * @throws BaseException
     */
    void importQuestion(MultipartFile file, Integer type) throws QuestionFileException;

    /**
     * 统计总数
     *
     * @return
     */
    int count(SurveyQuestionPO surveyQuestion) throws BaseException;


    List<SurveyQuestionPO> handleAnswerFiled(List<SurveyQuestionPO> questionList) throws BaseException;
}
