package tech.piis.modules.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyPaperQuestionPO;
import tech.piis.modules.survey.mapper.SurveyPaperQuestionMapper;
import tech.piis.modules.survey.service.ISurveyPaperQuestionService;

import java.util.Arrays;
import java.util.List;

/**
 * 试卷题目关系Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-09
 */
@Transactional
@Service
public class SurveyPaperQuestionServiceImpl implements ISurveyPaperQuestionService {
    @Autowired
    private SurveyPaperQuestionMapper surveyPaperQuestionMapper;

    /**
     * 查询试卷题目关系列表
     *
     * @param surveyPaperQuestion
     * @return
     * @throws BaseException
     */
    @Override
    public List<SurveyPaperQuestionPO> selectSurveyPaperQuestionList(SurveyPaperQuestionPO surveyPaperQuestion) throws BaseException {
        QueryWrapper<SurveyPaperQuestionPO> queryWrapper = new QueryWrapper<>();
        return surveyPaperQuestionMapper.selectList(queryWrapper);
    }

    /**
     * 新增试卷题目关系
     *
     * @param surveyPaperQuestion
     * @return
     * @throws BaseException
     */
    @Override
    public int save(SurveyPaperQuestionPO surveyPaperQuestion) throws BaseException {
        int result = surveyPaperQuestionMapper.insert(surveyPaperQuestion);
        return result;
    }

    /**
     * 根据ID修改试卷题目关系
     *
     * @param surveyPaperQuestion
     * @return
     * @throws BaseException
     */
    @Override
    public int update(SurveyPaperQuestionPO surveyPaperQuestion) throws BaseException {
        return surveyPaperQuestionMapper.updateById(surveyPaperQuestion);
    }

    /**
     * 根据ID批量删除试卷题目关系
     *
     * @param paperQuestionIds 试卷题目关系编号
     * @return
     */
    @Override
    public int deleteBySurveyPaperQuestionIds(Long[] paperQuestionIds) {
        List<Long> list = Arrays.asList(paperQuestionIds);
        return surveyPaperQuestionMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return surveyPaperQuestionMapper.selectCount(null);
    }
}
