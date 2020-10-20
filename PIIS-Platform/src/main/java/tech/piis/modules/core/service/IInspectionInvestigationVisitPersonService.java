package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPersonPO;
import java.util.List;

/**
 * 调研走访人员Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionInvestigationVisitPersonService {

    /**
     * 统计巡视方案下被巡视单位InspectionInvestigationVisitPerson次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionInvestigationVisitPersonCount(String planId) throws BaseException;

    /**
     * 查询调研走访人员列表
     * @param inspectionInvestigationVisitPerson
     * @return
     * @throws BaseException
     */
    List<InspectionInvestigationVisitPersonPO> selectInspectionInvestigationVisitPersonList(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException;

    /**
    * 新增调研走访人员
    * @param inspectionInvestigationVisitPerson
    * @return
    * @throws BaseException
    */
    int save(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException;

    /**
     * 根据ID修改调研走访人员
     * @param inspectionInvestigationVisitPerson
     * @return
     * @throws BaseException
     */
    int update(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException;

    /**
     * 根据ID批量删除调研走访人员
     * @param investigationVisitPersonIds 调研走访人员编号
     *
     * @return
     */
    int deleteByInspectionInvestigationVisitPersonIds(Long[] investigationVisitPersonIds);
}
