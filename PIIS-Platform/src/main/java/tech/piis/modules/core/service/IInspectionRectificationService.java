package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionRectificationPO;
import tech.piis.modules.core.domain.vo.RectificationCountVO;

import java.util.List;

/**
 * 整改公开情况Service接口
 *
 * @author Tuzki
 * @date 2020-12-11
 */
public interface IInspectionRectificationService {

    /**
     * 统计巡视方案下被巡视单位InspectionRectification次数
     *
     * @param planId 巡视计划ID
     */
    List<RectificationCountVO> selectInspectionRectificationCount(String planId) throws BaseException;

    /**
     * 查询整改公开情况列表
     *
     * @param inspectionRectification
     * @return
     * @throws BaseException
     */
    List<InspectionRectificationPO> selectInspectionRectificationList(InspectionRectificationPO inspectionRectification) throws BaseException;

    /**
     * 新增整改公开情况
     *
     * @param inspectionRectification
     * @return
     * @throws BaseException
     */
    int save(InspectionRectificationPO inspectionRectification) throws BaseException;

    /**
     * 根据ID修改整改公开情况
     *
     * @param inspectionRectification
     * @return
     * @throws BaseException
     */
    int update(InspectionRectificationPO inspectionRectification) throws BaseException;

    /**
     * 根据ID批量删除整改公开情况
     *
     * @param rectificationIds 整改公开情况编号
     * @return
     */
    int deleteByInspectionRectificationIds(Long[] rectificationIds) throws BaseException;


    /**
     * 批量审批
     *
     * @throws BaseException
     */
    void doApprovals(List<InspectionRectificationPO> inspectionRectificationList) throws BaseException;
}
