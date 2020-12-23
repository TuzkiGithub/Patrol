package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionProblemDraftPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;

/**
 * 问题底稿 Service接口
 *
 * @author Kevin
 * @date 2020-10-23
 */
public interface IInspectionProblemDraftService {

    /**
     * 统计巡视方案下被巡视单位InspectionProblemDraft次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionProblemDraftCount(String planId) throws BaseException;

    /**
     * 查询问题底稿 列表
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    List<InspectionProblemDraftPO> selectInspectionProblemDraftList(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException;

    /**
    * 新增问题底稿 
    * @param inspectionProblemDraft
    * @return
    * @throws BaseException
    */
    int save(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException;

    /**
     * 根据ID修改问题底稿 
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    int update(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException;

    /**
     * 根据ID批量删除问题底稿 
     * @param problemDraftIds 问题底稿 编号
     *
     * @return
     */
    int deleteByInspectionProblemDraftIds(Long[] problemDraftIds);

    /**
     * 统计总数
     * @return
     */
    int count();

    /**
     * 审批问题底稿
     * @param inspectionProblemDraftPOList
     */
    void doApprovals(List<InspectionProblemDraftPO> inspectionProblemDraftPOList);
}
