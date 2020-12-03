package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionOrganizationMeetingsPO;
import java.util.List;

/**
 * 组织会议Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionOrganizationMeetingsService {

    /**
     * 统计巡视方案下被巡视单位InspectionOrganizationMeetings次数
     * @param planId 巡视计划ID
     * @param organizationType 组织类型
     *
     */
    List<UnitsBizCountVO> selectInspectionOrganizationMeetingsCount(String planId, Integer organizationType) throws BaseException;

    /**
     * 查询组织会议列表
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    List<InspectionOrganizationMeetingsPO> selectInspectionOrganizationMeetingsList(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException;

    /**
     * 新增组织会议
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    int save(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException;

    /**
     * 根据ID修改组织会议
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    int update(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException;

    /**
     * 根据ID批量删除组织会议
     * @param organizationMeetingsIds 组织会议编号
     *
     * @return
     */
    int deleteByInspectionOrganizationMeetingsIds(Long[] organizationMeetingsIds) throws BaseException;

    /**
     * 审批
     * @param organizationMeetingsList
     */
    void doApprovals(List<InspectionOrganizationMeetingsPO> organizationMeetingsList);
}