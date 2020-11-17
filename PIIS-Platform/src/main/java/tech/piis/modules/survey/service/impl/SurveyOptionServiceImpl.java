package tech.piis.modules.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.service.IPiisDocumentService;
import tech.piis.modules.survey.domain.po.SurveyOptionPO;
import tech.piis.modules.survey.mapper.SurveyOptionMapper;
import tech.piis.modules.survey.service.ISurveyOptionService;

import java.util.Arrays;
import java.util.List;


/**
 * 选项Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-09
 */
@Transactional
@Service
public class SurveyOptionServiceImpl implements ISurveyOptionService {
    @Autowired
    private SurveyOptionMapper surveyOptionMapper;

    /**
     * 查询选项列表
     *
     * @param surveyOption
     * @return
     * @throws BaseException
     */
    @Override
    public List<SurveyOptionPO> selectSurveyOptionList(SurveyOptionPO surveyOption) throws BaseException {
        QueryWrapper<SurveyOptionPO> queryWrapper = new QueryWrapper<>();
        return surveyOptionMapper.selectList(queryWrapper);
    }

    /**
     * 新增选项
     *
     * @param surveyOption
     * @return
     * @throws BaseException
     */
    @Override
    public int save(SurveyOptionPO surveyOption) throws BaseException {
        int result = surveyOptionMapper.insert(surveyOption);
        return result;
    }

    /**
     * 根据ID修改选项
     *
     * @param surveyOption
     * @return
     * @throws BaseException
     */
    @Override
    public int update(SurveyOptionPO surveyOption) throws BaseException {
        return surveyOptionMapper.updateById(surveyOption);
    }

    /**
     * 根据ID批量删除选项
     *
     * @param optionIds 选项编号
     * @return
     */
    @Override
    public int deleteBySurveyOptionIds(Long[] optionIds) {
        List<Long> list = Arrays.asList(optionIds);
        return surveyOptionMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return surveyOptionMapper.selectCount(null);
    }
}
