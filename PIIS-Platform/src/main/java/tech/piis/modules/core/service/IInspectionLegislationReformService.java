package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionLegislationReformPO;
import java.util.List;

/**
 * 立行立改 Service接口
 *
 * @author Kevin
 * @date 2020-10-23
 */
public interface IInspectionLegislationReformService {

    /**
     * 统计巡视方案下被巡视单位InspectionLegislationReform次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionLegislationReformCount(String planId) throws BaseException;

    /**
     * 查询立行立改 列表
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    List<InspectionLegislationReformPO> selectInspectionLegislationReformList(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException;

    /**
    * 新增立行立改 
    * @param inspectionLegislationReform
    * @return
    * @throws BaseException
    */
    int save(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException;

    /**
     * 根据ID修改立行立改 
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    int update(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException;

    /**
     * 根据ID批量删除立行立改 
     * @param legislationReformIds 立行立改 编号
     *
     * @return
     */
    int deleteByInspectionLegislationReformIds(Long[] legislationReformIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
