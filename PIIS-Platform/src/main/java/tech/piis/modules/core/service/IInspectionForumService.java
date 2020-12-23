package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionForumPO;
import java.util.List;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

/**
 * 座谈会 Service接口
 *
 * @author Tuzki
 * @date 2020-12-07
 */
public interface IInspectionForumService {

    /**
     * 统计巡视方案下被巡视单位InspectionForum次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionForumCount(String planId) throws BaseException;

    /**
     * 查询座谈会 列表
     * @param inspectionForum
     * @return
     * @throws BaseException
     */
    List<InspectionForumPO> selectInspectionForumList(InspectionForumPO inspectionForum) throws BaseException;

    /**
    * 新增座谈会 
    * @param inspectionForum
    * @return
    * @throws BaseException
    */
    int save(InspectionForumPO inspectionForum) throws BaseException;

    /**
     * 根据ID修改座谈会 
     * @param inspectionForum
     * @return
     * @throws BaseException
     */
    int update(InspectionForumPO inspectionForum) throws BaseException;

    /**
     * 根据ID批量删除座谈会 
     * @param forumIds 座谈会 编号
     *
     * @return
     */
    int deleteByInspectionForumIds(Long[] forumIds) throws BaseException;



    /**
    * 批量审批
    * @throws BaseException
    */
    void doApprovals(List<InspectionForumPO> inspectionForumList) throws BaseException;
}
