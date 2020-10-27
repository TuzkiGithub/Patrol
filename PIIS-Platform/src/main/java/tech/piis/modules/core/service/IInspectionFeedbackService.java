package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import java.util.List;

/**
 * 反馈意见 Service接口
 *
 * @author Tuzki
 * @date 2020-10-27
 */
public interface IInspectionFeedbackService {

    /**
     * 统计巡视方案下被巡视单位InspectionFeedback次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionFeedbackCount(String planId) throws BaseException;

    /**
     * 查询反馈意见 列表
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    List<InspectionFeedbackPO> selectInspectionFeedbackList(InspectionFeedbackPO inspectionFeedback) throws BaseException;

    /**
    * 新增反馈意见 
    * @param inspectionFeedback
    * @return
    * @throws BaseException
    */
    int save(InspectionFeedbackPO inspectionFeedback) throws BaseException;

    /**
     * 根据ID修改反馈意见 
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    int update(InspectionFeedbackPO inspectionFeedback) throws BaseException;

    /**
     * 根据ID批量删除反馈意见 
     * @param feedbackIds 反馈意见 编号
     *
     * @return
     */
    int deleteByInspectionFeedbackIds(String[] feedbackIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
