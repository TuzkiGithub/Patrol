package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionCheckPersonMattersPO;
import java.util.List;

/**
 * 抽查个人事项报告Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionCheckPersonMattersService {

    /**
     * 统计巡视方案下被巡视单位InspectionCheckPersonMatters次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionCheckPersonMattersCount(String planId) throws BaseException;

    /**
     * 查询抽查个人事项报告列表
     * @param inspectionCheckPersonMatters
     * @return
     * @throws BaseException
     */
    List<InspectionCheckPersonMattersPO> selectInspectionCheckPersonMattersList(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException;


    /**
     * 查询总数
     * @param unitsId 被巡视单位
     * @return
     * @throws BaseException
     */
    int count(Long unitsId) throws BaseException;

    /**
    * 新增抽查个人事项报告
    * @param inspectionCheckPersonMatters
    * @return
    * @throws BaseException
    */
    int save(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException;

    /**
     * 根据ID修改抽查个人事项报告
     * @param inspectionCheckPersonMatters
     * @return
     * @throws BaseException
     */
    int update(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException;

    /**
     * 根据ID批量删除抽查个人事项报告
     * @param checkPersonMattersIds 抽查个人事项报告编号
     *
     * @return
     */
    int deleteByInspectionCheckPersonMattersIds(Long[] checkPersonMattersIds);
}
