package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionCommitteeMeetingsPO;
import java.util.List;

/**
 * 党委会小组会纪要 Service接口
 *
 * @author Tuzki
 * @date 2020-10-27
 */
public interface IInspectionCommitteeMeetingsService {

    /**
     * 统计巡视方案下被巡视单位InspectionCommitteeMeetings次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionCommitteeMeetingsCount(String planId) throws BaseException;

    /**
     * 查询党委会小组会纪要 列表
     * @param inspectionCommitteeMeetings
     * @return
     * @throws BaseException
     */
    List<InspectionCommitteeMeetingsPO> selectInspectionCommitteeMeetingsList(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException;

    /**
    * 新增党委会小组会纪要 
    * @param inspectionCommitteeMeetings
    * @return
    * @throws BaseException
    */
    int save(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException;

    /**
     * 根据ID修改党委会小组会纪要 
     * @param inspectionCommitteeMeetings
     * @return
     * @throws BaseException
     */
    int update(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException;

    /**
     * 根据ID批量删除党委会小组会纪要 
     * @param committeeMeetingsIds 党委会小组会纪要 编号
     *
     * @return
     */
    int deleteByInspectionCommitteeMeetingsIds(Long[] committeeMeetingsIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
