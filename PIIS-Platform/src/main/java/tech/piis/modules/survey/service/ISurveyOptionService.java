package tech.piis.modules.survey.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.survey.domain.po.SurveyOptionPO;

import java.util.List;

/**
 * 选项Service接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface ISurveyOptionService {

    /**
     * 查询选项列表
     *
     * @param surveyOption
     * @return
     * @throws BaseException
     */
    List<SurveyOptionPO> selectSurveyOptionList(SurveyOptionPO surveyOption) throws BaseException;

    /**
     * 新增选项
     *
     * @param surveyOption
     * @return
     * @throws BaseException
     */
    int save(SurveyOptionPO surveyOption) throws BaseException;

    /**
     * 根据ID修改选项
     *
     * @param surveyOption
     * @return
     * @throws BaseException
     */
    int update(SurveyOptionPO surveyOption) throws BaseException;

    /**
     * 根据ID批量删除选项
     *
     * @param optionIds 选项编号
     * @return
     */
    int deleteBySurveyOptionIds(Long[] optionIds);

    /**
     * 统计总数
     *
     * @return
     */
    int count();
}
