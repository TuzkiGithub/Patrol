package tech.piis.modules.survey.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.enums.PaperTypeEnum;
import tech.piis.common.enums.QuestionTypeEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.exception.file.QuestionFileException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.modules.survey.domain.po.SurveyOptionPO;
import tech.piis.modules.survey.domain.po.SurveyQuestionPO;
import tech.piis.modules.survey.mapper.SurveyOptionMapper;
import tech.piis.modules.survey.mapper.SurveyQuestionMapper;
import tech.piis.modules.survey.service.ISurveyQuestionService;

import java.util.*;

import static tech.piis.common.constant.GenConstants.*;

/**
 * 题目Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-09
 */
@Transactional
@Service
public class SurveyQuestionServiceImpl implements ISurveyQuestionService {
    @Autowired
    private SurveyQuestionMapper surveyQuestionMapper;

    @Autowired
    private SurveyOptionMapper surveyOptionMapper;

    /**
     * 查询题目列表
     *
     * @param surveyQuestion
     * @return
     * @throws BaseException
     */
    @Override
    public List<SurveyQuestionPO> selectSurveyQuestionList(SurveyQuestionPO surveyQuestion) throws BaseException {
        return handleAnswerFiled(surveyQuestionMapper.selectSurveyQuestionList(surveyQuestion));
    }

    /**
     * 新增题目
     *
     * @param surveyQuestion
     * @return
     * @throws BaseException
     */
    @Override
    public int save(SurveyQuestionPO surveyQuestion) throws BaseException {
        String questionId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        surveyQuestion.setQuestionId(questionId);
        List<SurveyOptionPO> optionList = surveyQuestion.getOptionList();
        Map<String, String> optionMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(optionList)) {
            optionList.forEach(option -> {
                option.setQuestionId(questionId);
                surveyOptionMapper.insert(option);
                String key = questionId + LINE_CHAR + option.getOptionName();
                optionMap.put(key, String.valueOf(option.getOptionId()));
            });
        }
        convertReferAnswer(surveyQuestion, optionMap);
        return surveyQuestionMapper.insert(surveyQuestion.setOptionList(null));
    }


    /**
     * 根据ID修改题目
     *
     * @param surveyQuestion
     * @return
     * @throws BaseException
     */
    @Override
    public int update(SurveyQuestionPO surveyQuestion) throws BaseException {
        List<SurveyOptionPO> optionList = surveyQuestion.getOptionList();

        //删除原有题目关联的选项
        QueryWrapper<SurveyOptionPO> delQueryWrapper = new QueryWrapper<>();
        delQueryWrapper.eq("QUESTION_ID", surveyQuestion.getQuestionId());
        surveyOptionMapper.delete(delQueryWrapper);

        //新增题目关联的选项
        Map<String, String> optionMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(optionList)) {
            optionList.forEach(option -> {
                String questionId = surveyQuestion.getQuestionId();
                option.setQuestionId(questionId);
                surveyOptionMapper.insert(option);
                String key = questionId + LINE_CHAR + option.getOptionName();
                optionMap.put(key, String.valueOf(option.getOptionId()));
            });
        }
        convertReferAnswer(surveyQuestion, optionMap);
        return surveyQuestionMapper.updateById(surveyQuestion.setOptionList(null));
    }

    /**
     * 根据ID批量删除题目
     *
     * @param questionIds 题目编号
     * @return
     */
    @Override
    public int deleteBySurveyQuestionIds(String[] questionIds) {
        List<String> list = Arrays.asList(questionIds);
        int row = surveyQuestionMapper.deleteBatchIds(list);
        //删除关联的选项
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(questionId -> {
                QueryWrapper<SurveyOptionPO> delQueryWrapper = new QueryWrapper<>();
                delQueryWrapper.eq("QUESTION_ID", questionId);
                surveyOptionMapper.delete(delQueryWrapper);
            });
        }
        return row;
    }

    /**
     * 通过模板导入题目
     *
     * @param file
     * @throws BaseException
     */
    @Override
    public void importQuestion(MultipartFile file, Integer type) throws QuestionFileException {
        ImportParams params = new ImportParams();
        params.setHeadRows(2);
        params.setTitleRows(0);
        List<SurveyQuestionPO> questionList = null;
        try {
            questionList = ExcelImportUtil.importExcel(file.getInputStream(), SurveyQuestionPO.class, params);
        } catch (Exception e) {
            throw new QuestionFileException("导入数据错误！");
        }
        //校验字段
        validQuestion(questionList);
        List<SurveyOptionPO> optionList = new ArrayList<>();
        Map<String, String> optionMap = new HashMap<>();
        //填充实体
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> {
                String questionId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
                Integer questionType = question.getQuestionType();
                question.setQuestionId(questionId);
                question.setRangeId(BizUtils.getOrgIdByName(question.getRangeName()));
                question.setType(type);
                BizUtils.setCreatedOperation(SurveyQuestionPO.class, question);
                List<SurveyOptionPO> tempOptionList = question.getOptionList();
                if (!CollectionUtils.isEmpty(tempOptionList)) {
                    tempOptionList.forEach(option -> {
                        if (QuestionTypeEnum.SINGLE.getCode().equals(questionType) || QuestionTypeEnum.DOUBLE.getCode().equals(questionType)) {
                            option.setQuestionId(questionId);
                        }
                        optionMap.put(questionId + LINE_CHAR, option.getOptionName());
                    });
                }
                optionList.addAll(tempOptionList);
                question.setOptionList(null);
            });
        }
        BizUtils.filterOptionName(optionList);
        //批量导入题目，选项
        surveyOptionMapper.insertOptionBatch(optionList);
        convertReferAnswer(questionList, optionMap);
        surveyQuestionMapper.insertQuestionBatch(questionList);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(SurveyQuestionPO surveyQuestion) {
        Integer type = surveyQuestion.getType();
        QueryWrapper<SurveyQuestionPO> questionPOQueryWrapper = new QueryWrapper<>();
        questionPOQueryWrapper.eq("TYPE", type);
        return surveyQuestionMapper.selectCount(questionPOQueryWrapper);
    }

    /**
     * 处理多选题中的参考答案字段
     *
     * @param questionList
     * @throws BaseException
     */
    @Override
    public List<SurveyQuestionPO> handleAnswerFiled(List<SurveyQuestionPO> questionList) throws BaseException {
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> {
                String referenceAnswer = question.getReferenceAnswer();
                if (QuestionTypeEnum.DOUBLE.getCode().equals(question.getQuestionType())) {
                    referenceAnswer = referenceAnswer.replace(REPLACE_CHAR, SPLIT_CHAR);
                    question.setReferenceAnswer(referenceAnswer);
                }
            });
        }
        return questionList;
    }

    /**
     * 校验题目必填字段
     *
     * @param questionList
     */
    private void validQuestion(List<SurveyQuestionPO> questionList) {
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> {
                String questionName = question.getQuestionName();
                String rangeName = question.getRangeName();
                Integer questionType = question.getQuestionType();
                Integer requiredFlag = question.getRequiredFlag();
                Integer businessType = question.getBusinessType();
                String answer = question.getReferenceAnswer();
                if (StringUtils.isEmpty(questionName)) {
                    throw new QuestionFileException("题目不能为空！");
                }
                if (StringUtils.isEmpty(rangeName)) {
                    throw new QuestionFileException("适用范围不能为空！");
                }
                if (null == questionType) {
                    throw new QuestionFileException("题目类型不能为空！");
                }
                if (null == requiredFlag) {
                    throw new QuestionFileException("是否必填不能为空！");
                }
                if (null == businessType) {
                    throw new QuestionFileException("题目业务类型不能为空！");
                }
                if (PaperTypeEnum.EVALUATION.getCode().equals(question.getType())) {
                    if (StringUtils.isEmpty(answer)) {
                        throw new QuestionFileException("参考答案不能为空！");
                    }
                }

                List<SurveyOptionPO> optionList = question.getOptionList();
                if (QuestionTypeEnum.SINGLE.getCode().equals(questionType)) {
                    if (CollectionUtils.isEmpty(optionList)) {
                        throw new QuestionFileException("单选题选项不能为空！");
                    }
                }

                if (QuestionTypeEnum.DOUBLE.getCode().equals(questionType)) {
                    if (CollectionUtils.isEmpty(optionList)) {
                        throw new QuestionFileException("多选题选项不能为空！");
                    }
                }
            });
        } else {
            throw new QuestionFileException("题目不能为空！");
        }
    }

    /**
     * 批量处理参考答案字段
     *
     * @param questionList 题目实体
     * @param optionMap    选项名称和ID map
     */
    public void convertReferAnswer(List<SurveyQuestionPO> questionList, Map<String, String> optionMap) {
        if (!CollectionUtils.isEmpty(questionList)) {
            questionList.forEach(question -> convertReferAnswer(question, optionMap));
        }
    }

    /**
     * 处理参考答案字段
     *
     * @param surveyQuestion 题目实体
     * @param optionMap      选项名称和ID map
     */
    private void convertReferAnswer(SurveyQuestionPO surveyQuestion, Map<String, String> optionMap) {
        //处理参考答案字段
        Integer questionType = surveyQuestion.getQuestionType();
        String questionId = surveyQuestion.getQuestionId();
        String referenceAnswer = surveyQuestion.getReferenceAnswer();
        if (QuestionTypeEnum.SINGLE.getCode().equals(questionType)) {
            String key = questionId + LINE_CHAR + referenceAnswer;
            if (optionMap.containsKey(key)) {
                surveyQuestion.setReferenceAnswer(optionMap.get(key));
            }
        }
        if (QuestionTypeEnum.DOUBLE.getCode().equals(questionType)) {
            String[] optionNames;
            optionNames = referenceAnswer.split(REPLACE_CHAR);
            StringBuilder questionOptionIds = new StringBuilder();
            for (String temp : optionNames) {
                String key = questionId + LINE_CHAR + temp;
                if (optionMap.containsKey(key)) {
                    questionOptionIds.append(optionMap.get(key)).append(SPLIT_CHAR);
                }
            }
            String questionOptionIdsStr = questionOptionIds.toString();
            questionOptionIdsStr = questionOptionIdsStr.substring(0, questionOptionIdsStr.lastIndexOf(SPLIT_CHAR));
            surveyQuestion.setReferenceAnswer(questionOptionIdsStr);
        }

    }

}
