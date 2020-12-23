package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionMeetingsResearchPO;
import java.util.List;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

/**
 * 会议研究Service接口
 *
 * @author Tuzki
 * @date 2020-12-11
 */
public interface IInspectionMeetingsResearchService {

    /**
     * 统计巡视方案下被巡视单位InspectionMeetingsResearch次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionMeetingsResearchCount(String planId) throws BaseException;

    /**
     * 查询会议研究列表
     * @param inspectionMeetingsResearch
     * @return
     * @throws BaseException
     */
    List<InspectionMeetingsResearchPO> selectInspectionMeetingsResearchList(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException;

    /**
    * 新增会议研究
    * @param inspectionMeetingsResearch
    * @return
    * @throws BaseException
    */
    int save(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException;

    /**
     * 根据ID修改会议研究
     * @param inspectionMeetingsResearch
     * @return
     * @throws BaseException
     */
    int update(InspectionMeetingsResearchPO inspectionMeetingsResearch) throws BaseException;

    /**
     * 根据ID批量删除会议研究
     * @param meetingsResearchIds 会议研究编号
     *
     * @return
     */
    int deleteByInspectionMeetingsResearchIds(Long[] meetingsResearchIds) throws BaseException;



    /**
    * 批量审批
    * @throws BaseException
    */
    void doApprovals(List<InspectionMeetingsResearchPO> inspectionMeetingsResearchList) throws BaseException;
}
