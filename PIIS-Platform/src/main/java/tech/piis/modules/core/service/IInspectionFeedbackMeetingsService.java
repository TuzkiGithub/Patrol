package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionFeedbackMeetingsPO;
import java.util.List;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

/**
 * 反馈会Service接口
 *
 * @author Tuzki
 * @date 2020-12-11
 */
public interface IInspectionFeedbackMeetingsService {

    /**
     * 统计巡视方案下被巡视单位InspectionFeedbackMeetings次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionFeedbackMeetingsCount(String planId) throws BaseException;

    /**
     * 查询反馈会列表
     * @param inspectionFeedbackMeetings
     * @return
     * @throws BaseException
     */
    List<InspectionFeedbackMeetingsPO> selectInspectionFeedbackMeetingsList(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException;

    /**
    * 新增反馈会
    * @param inspectionFeedbackMeetings
    * @return
    * @throws BaseException
    */
    int save(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException;

    /**
     * 根据ID修改反馈会
     * @param inspectionFeedbackMeetings
     * @return
     * @throws BaseException
     */
    int update(InspectionFeedbackMeetingsPO inspectionFeedbackMeetings) throws BaseException;

    /**
     * 根据ID批量删除反馈会
     * @param feedbackMeetingsIds 反馈会编号
     *
     * @return
     */
    int deleteByInspectionFeedbackMeetingsIds(Long[] feedbackMeetingsIds) throws BaseException;



    /**
    * 批量审批
    * @throws BaseException
    */
    void doApprovals(List<InspectionFeedbackMeetingsPO> inspectionFeedbackMeetingsList) throws BaseException;
}
