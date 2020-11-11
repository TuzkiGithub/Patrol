package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import tech.piis.modules.core.domain.po.InspectionFeedbackQuestionsPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionFeedbackMapper;
import tech.piis.modules.core.mapper.InspectionFeedbackQuestionsMapper;
import tech.piis.modules.core.service.IInspectionFeedbackService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * 反馈意见 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Transactional
@Service
public class InspectionFeedbackServiceImpl implements IInspectionFeedbackService {
    @Autowired
    private InspectionFeedbackMapper inspectionFeedbackMapper;

    @Autowired
    private InspectionFeedbackQuestionsMapper inspectionFeedbackQuestionsMapper;

    /**
     * 统计巡视方案下被巡视单位InspectionFeedback次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionFeedbackCount(String planId) throws BaseException {
        return inspectionFeedbackMapper.selectInspectionFeedbackCount(planId);
    }

    /**
     * 查询反馈意见 列表
     *
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionFeedbackPO> selectInspectionFeedbackList(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        return inspectionFeedbackMapper.selectInspectionFeedbackList(inspectionFeedback);
    }

    /**
     * 新增反馈意见
     *
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        //设置主键
        String feedbackId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionFeedback.setFeedbackId(feedbackId);

        //新增问题清单
        List<InspectionFeedbackQuestionsPO> feedbackQuestionsList = inspectionFeedback.getFeedbackQuestionsList();
        if (!CollectionUtils.isEmpty(feedbackQuestionsList)) {
            feedbackQuestionsList.forEach(var -> {
                var.setFeedbackId(feedbackId);
                inspectionFeedbackQuestionsMapper.insert(var);
            });
        }
        return inspectionFeedbackMapper.insert(inspectionFeedback.setFeedbackQuestionsList(null));
    }

    /**
     * 根据ID修改反馈意见
     *
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        //修改问题清单

        List<InspectionFeedbackQuestionsPO> feedbackQuestionsList = inspectionFeedback.getFeedbackQuestionsList();
        if (!CollectionUtils.isEmpty(feedbackQuestionsList)) {
            feedbackQuestionsList.forEach(feedbackQuestion -> {
                feedbackQuestion.setFeedbackId(inspectionFeedback.getFeedbackId());
                Integer operationType = feedbackQuestion.getOperationType();
                      if (null != operationType) {
                switch (operationType) {
                    case INSERT:
                        inspectionFeedbackQuestionsMapper.insert(feedbackQuestion);
                        break;
                    case UPDATE:
                        inspectionFeedbackQuestionsMapper.updateById(feedbackQuestion);
                        break;
                    case DELETE:
                        inspectionFeedbackQuestionsMapper.deleteById(feedbackQuestion.getFeedbackQuestionsId());
                        break;
                }
            }
        });

        }
        return inspectionFeedbackMapper.updateById(inspectionFeedback.setFeedbackQuestionsList(null));
    }

    /**
     * 根据ID批量删除反馈意见
     *
     * @param feedbackIds 反馈意见 编号
     * @return
     */
    @Override
    public int deleteByInspectionFeedbackIds(String[] feedbackIds) {
        List<String> list = Arrays.asList(feedbackIds);
        for (String feedbackId : feedbackIds) {
            QueryWrapper<InspectionFeedbackQuestionsPO> questionsPOQueryWrapper = new QueryWrapper<>();
            questionsPOQueryWrapper.eq("FEEDBACK_ID", feedbackId);
            inspectionFeedbackQuestionsMapper.delete(questionsPOQueryWrapper);
        }
        return inspectionFeedbackMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(InspectionFeedbackPO inspectionFeedback) {
        QueryWrapper<InspectionFeedbackPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionFeedback.getUnitsId());
        queryWrapper.eq("plan_id", inspectionFeedback.getPlanId());
        return inspectionFeedbackMapper.selectCount(queryWrapper);
    }
}
