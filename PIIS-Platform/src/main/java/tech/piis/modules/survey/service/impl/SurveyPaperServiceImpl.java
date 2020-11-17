package tech.piis.modules.survey.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.enums.QuestionTypeEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.modules.survey.domain.po.SurveyOptionPO;
import tech.piis.modules.survey.domain.po.SurveyPaperPO;
import tech.piis.modules.survey.domain.po.SurveyPaperQuestionPO;
import tech.piis.modules.survey.domain.po.SurveyQuestionPO;
import tech.piis.modules.survey.mapper.SurveyPaperMapper;
import tech.piis.modules.survey.mapper.SurveyPaperQuestionMapper;
import tech.piis.modules.survey.mapper.SurveyQuestionMapper;
import tech.piis.modules.survey.service.ISurveyPaperService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 试卷/问卷Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-09
 */
@Transactional
@Service
public class SurveyPaperServiceImpl implements ISurveyPaperService {
    @Autowired
    private SurveyPaperMapper surveyPaperMapper;

    @Autowired
    private SurveyPaperQuestionMapper surveyPaperQuestionMapper;

    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;


    /**
     * 查询试卷/问卷列表
     *
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    @Override
    public List<SurveyPaperPO> selectSurveyPaperList(SurveyPaperPO surveyPaper) throws BaseException {
        QueryWrapper<SurveyPaperPO> queryWrapper = new QueryWrapper<>();
        return surveyPaperMapper.selectList(queryWrapper);
    }

    /**
     * 根据ID修改试卷/问卷
     *
     * @param paper
     * @return
     * @throws BaseException
     */
    @Override
    public int update(SurveyPaperPO paper) throws BaseException {
        //删除原来的试卷-题目关联
        QueryWrapper<SurveyPaperQuestionPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PAPER_ID", paper.getPaperId());
        surveyPaperQuestionMapper.delete(queryWrapper);

        //建立新的试卷-题目关联
        List<SurveyPaperQuestionPO> paperQuestionList = new ArrayList<>();
        List<SurveyQuestionPO> questionList = paper.getQuestionList();
        buildPaperQuestionRelation(paper, paperQuestionList, questionList);
        surveyPaperQuestionMapper.insertPaperQuestionBatch(paperQuestionList);
        return surveyPaperMapper.updateById(paper.setQuestionList(null));
    }

    /**
     * 根据ID批量删除试卷/问卷
     *
     * @param paperIds 试卷/问卷编号
     * @return
     */
    @Override
    public int deleteBySurveyPaperIds(String[] paperIds) {
        List<String> paperIdList = Arrays.asList(paperIds);
        paperIdList.forEach(paperId -> {
            QueryWrapper<SurveyPaperQuestionPO> questionPOQueryWrapper = new QueryWrapper<>();
            questionPOQueryWrapper.eq("PAPER_ID", paperId);
            surveyPaperQuestionMapper.delete(questionPOQueryWrapper);
        });
        return surveyPaperMapper.deleteBatchIds(paperIdList);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(SurveyPaperPO surveyPaper) {
        Integer paperType = surveyPaper.getPaperType();
        QueryWrapper<SurveyPaperPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PAPER_TYPE", paperType);
        return surveyPaperMapper.selectCount(queryWrapper);
    }

    /**
     * 创建试卷，从题库中选择
     *
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    @Override
    public int saveBySelect(SurveyPaperPO surveyPaper) throws BaseException {
        String id = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        surveyPaper.setPaperId(id);
        List<SurveyPaperQuestionPO> paperQuestionList = new ArrayList<>();
        List<SurveyQuestionPO> questionList = surveyPaper.getQuestionList();
        //新增试卷
        int row = surveyPaperMapper.insert(surveyPaper.setQuestionList(null));
        //新增试卷、题目关联
        buildPaperQuestionRelation(surveyPaper, paperQuestionList, questionList);
        surveyPaperQuestionMapper.insertPaperQuestionBatch(paperQuestionList);
        return row;
    }

    /**
     * 创建试卷，系统生成
     *
     * @param surveyPaper
     * @return
     * @throws BaseException
     */
    @Override
    public void saveByCreate(SurveyPaperPO surveyPaper) throws BaseException {
        List<SurveyPaperQuestionPO> paperQuestionList = new ArrayList<>();
        List<SurveyQuestionPO> questionList = surveyQuestionMapper.selectQuestionRandom();

        //新增试卷
        String paperId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        SurveyPaperPO paper = new SurveyPaperPO()
                .setPaperId(paperId);
        surveyPaperMapper.insert(paper);
        buildPaperQuestionRelation(paper, paperQuestionList, questionList);
        //新增试卷、题目关联
        surveyPaperQuestionMapper.insertPaperQuestionBatch(paperQuestionList);
    }

    /**
     * 创建试卷，导入题库
     *
     * @param paperType
     * @param file
     * @return
     * @throws BaseException
     */
    @Override
    public void saveByImport(Integer paperType, MultipartFile file) throws BaseException {
    }


    /**
     * 打乱选项顺序
     */
    private void disorderOptionOrder(List<SurveyQuestionPO> questionList) {
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> {
                Integer type = question.getQuestionType();
                if (QuestionTypeEnum.SINGLE.getCode().equals(type) || QuestionTypeEnum.DOUBLE.getCode().equals(type)) {
                    List<SurveyOptionPO> optionList = question.getOptionList();
                    if (!CollectionUtils.isEmpty(optionList))
                        Collections.shuffle(optionList);
                }
            });
        }
    }


    /**
     * 去除答案属性
     *
     * @param paper
     */
    private void removePaperAttr(SurveyPaperPO paper) {
        List<SurveyQuestionPO> questionList = paper.getQuestionList();
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> question.setReferenceAnswer(null));
        }

    }


    /**
     * 建立试卷，题目关联
     */
    private void buildPaperQuestionRelation(SurveyPaperPO paper, List<SurveyPaperQuestionPO> paperQuestionList, List<SurveyQuestionPO> questionList) {
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> {
                SurveyPaperQuestionPO surveyPaperQuestion = new SurveyPaperQuestionPO();
                surveyPaperQuestion.setPaperId(paper.getPaperId());
                surveyPaperQuestion.setQuestionId(question.getQuestionId());
                paperQuestionList.add(surveyPaperQuestion);
            });
        }
    }
}
