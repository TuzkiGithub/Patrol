package tech.piis.modules.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyResultPO;
import tech.piis.modules.survey.mapper.SurveyResultMapper;
import tech.piis.modules.survey.service.ISurveyResultService;

import java.util.Arrays;
import java.util.List;


/**
 * 试卷/问卷回答情况Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-18
 */
@Transactional
@Service
public class SurveyResultServiceImpl implements ISurveyResultService {
    @Autowired
    private SurveyResultMapper surveyResultMapper;

    /**
     * 查询试卷/问卷回答情况列表
     *
     * @param surveyResult
     * @return
     * @throws BaseException
     */
    @Override
    public List<SurveyResultPO> selectSurveyResultList(SurveyResultPO surveyResult) throws BaseException {
        QueryWrapper<SurveyResultPO> queryWrapper = new QueryWrapper<>();
        return surveyResultMapper.selectList(queryWrapper);
    }

    /**
     * 新增试卷/问卷回答情况
     *
     * @param surveyResult
     * @return
     * @throws BaseException
     */
    @Override
    public int save(SurveyResultPO surveyResult) throws BaseException {
        int result = surveyResultMapper.insert(surveyResult);
        return result;
    }

    /**
     * 根据ID修改试卷/问卷回答情况
     *
     * @param surveyResult
     * @return
     * @throws BaseException
     */
    @Override
    public int update(SurveyResultPO surveyResult) throws BaseException {
        return surveyResultMapper.updateById(surveyResult);
    }

    /**
     * 根据ID批量删除试卷/问卷回答情况
     *
     * @param resultIds 试卷/问卷回答情况编号
     * @return
     */
    @Override
    public int deleteBySurveyResultIds(Long[] resultIds) {
        List<Long> list = Arrays.asList(resultIds);
        return surveyResultMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return surveyResultMapper.selectCount(null);
    }
}