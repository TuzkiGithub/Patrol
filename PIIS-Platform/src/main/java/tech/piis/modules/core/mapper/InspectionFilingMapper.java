package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionFilingPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.mapper
 * User: Tuzki
 * Date: 2020/11/3
 * Time: 19:06
 * Description:
 */
public interface InspectionFilingMapper {
    /**
     * 根据巡视计划ID查询总览
     * @param planId
     * @return
     */
    List<InspectionFilingPO> selectFilingByPlanId(String planId);
}
