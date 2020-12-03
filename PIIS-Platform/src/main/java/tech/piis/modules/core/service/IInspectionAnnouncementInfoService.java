package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionAnnouncementInfoPO;
import java.util.List;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

/**
 * 公告信息 Service接口
 *
 * @author Tuzki
 * @date 2020-12-03
 */
public interface IInspectionAnnouncementInfoService {

    /**
     * 统计巡视方案下被巡视单位InspectionAnnouncementInfo次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionAnnouncementInfoCount(String planId) throws BaseException;

    /**
     * 查询公告信息 列表
     * @param inspectionAnnouncementInfo
     * @return
     * @throws BaseException
     */
    List<InspectionAnnouncementInfoPO> selectInspectionAnnouncementInfoList(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException;

    /**
    * 新增公告信息 
    * @param inspectionAnnouncementInfo
    * @return
    * @throws BaseException
    */
    int save(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException;

    /**
     * 根据ID修改公告信息 
     * @param inspectionAnnouncementInfo
     * @return
     * @throws BaseException
     */
    int update(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException;

    /**
     * 根据ID批量删除公告信息 
     * @param announcementInfoIds 公告信息 编号
     *
     * @return
     */
    int deleteByInspectionAnnouncementInfoIds(Long[] announcementInfoIds) throws BaseException;



    /**
    * 批量审批
    * @throws BaseException
    */
    void doApprovals(List<InspectionAnnouncementInfoPO> inspectionAnnouncementInfoList) throws BaseException;
}
