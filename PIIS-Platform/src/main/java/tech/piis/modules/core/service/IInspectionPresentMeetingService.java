package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionPresentMeetingPO;
import java.util.List;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

/**
 * 列席会议 Service接口
 *
 * @author Tuzki
 * @date 2020-12-07
 */
public interface IInspectionPresentMeetingService {

    /**
     * 统计巡视方案下被巡视单位InspectionPresentMeeting次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionPresentMeetingCount(String planId) throws BaseException;

    /**
     * 查询列席会议 列表
     * @param inspectionPresentMeeting
     * @return
     * @throws BaseException
     */
    List<InspectionPresentMeetingPO> selectInspectionPresentMeetingList(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException;

    /**
    * 新增列席会议 
    * @param inspectionPresentMeeting
    * @return
    * @throws BaseException
    */
    int save(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException;

    /**
     * 根据ID修改列席会议 
     * @param inspectionPresentMeeting
     * @return
     * @throws BaseException
     */
    int update(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException;

    /**
     * 根据ID批量删除列席会议 
     * @param presentMeetingIds 列席会议 编号
     *
     * @return
     */
    int deleteByInspectionPresentMeetingIds(Long[] presentMeetingIds) throws BaseException;



    /**
    * 批量审批
    * @throws BaseException
    */
    void doApprovals(List<InspectionPresentMeetingPO> inspectionPresentMeetingList) throws BaseException;
}
