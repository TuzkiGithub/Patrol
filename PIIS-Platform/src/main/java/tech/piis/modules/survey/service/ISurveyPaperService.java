package tech.piis.modules.survey.service;

import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyPaperPO;

import java.util.List;

/**
 * 试卷/问卷Service接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface ISurveyPaperService {

    /**
     * 查询试卷/问卷列表
     *
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    List<SurveyPaperPO> selectSurveyPaperList(SurveyPaperPO surveyPaper) throws BaseException;

    /**
     * 根据ID修改试卷/问卷
     *
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    int update(SurveyPaperPO surveyPaper) throws BaseException;

    /**
     * 根据ID批量删除试卷/问卷
     *
     * @param paperIds 试卷/问卷编号
     * @return
     */
    int deleteBySurveyPaperIds(String[] paperIds);

    /**
     * 统计总数
     *
     * @return
     */
    int count(SurveyPaperPO surveyPaper);

    /**创建试卷，从题库中选择
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    int saveBySelect(SurveyPaperPO surveyPaper) throws BaseException;

    /**
     * 创建试卷，系统生成
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    void saveByCreate(SurveyPaperPO surveyPaper) throws BaseException;

    /**
     * 创建试卷，导入模板
     * @param paperType
     * @param file
     * @return
     * @throws BaseException
     */
    void saveByImport(Integer paperType, MultipartFile file) throws BaseException;
}
