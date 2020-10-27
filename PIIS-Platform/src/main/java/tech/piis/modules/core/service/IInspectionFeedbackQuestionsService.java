package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionFeedbackQuestionsPO;
import java.util.List;

/**
 * 反馈问题清单 Service接口
 *
 * @author Tuzki
 * @date 2020-10-27
 */
public interface IInspectionFeedbackQuestionsService {

    /**
     * 统计巡视方案下被巡视单位InspectionFeedbackQuestions次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionFeedbackQuestionsCount(String planId) throws BaseException;

    /**
     * 查询反馈问题清单 列表
     * @param inspectionFeedbackQuestions
     * @return
     * @throws BaseException
     */
    List<InspectionFeedbackQuestionsPO> selectInspectionFeedbackQuestionsList(InspectionFeedbackQuestionsPO inspectionFeedbackQuestions) throws BaseException;

    /**
    * 新增反馈问题清单 
    * @param inspectionFeedbackQuestions
    * @return
    * @throws BaseException
    */
    int save(InspectionFeedbackQuestionsPO inspectionFeedbackQuestions) throws BaseException;

    /**
     * 根据ID修改反馈问题清单 
     * @param inspectionFeedbackQuestions
     * @return
     * @throws BaseException
     */
    int update(InspectionFeedbackQuestionsPO inspectionFeedbackQuestions) throws BaseException;

    /**
     * 根据ID批量删除反馈问题清单 
     * @param feedbackQuestionsIds 反馈问题清单 编号
     *
     * @return
     */
    int deleteByInspectionFeedbackQuestionsIds(Long[] feedbackQuestionsIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
