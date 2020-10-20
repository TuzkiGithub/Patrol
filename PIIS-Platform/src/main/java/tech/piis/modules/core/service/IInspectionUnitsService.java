package tech.piis.modules.core.service;


import tech.piis.modules.core.domain.po.InspectionUnitsPO;

import java.util.List;

/**
 * 被巡视单位 Service接口
 *
 * @author Tuzki
 * @date 2020-09-14
 */
public interface IInspectionUnitsService {
    /**
     * 根据被巡视单位ID查询被巡视单位下的组员信息
     *
     * @param unitsId
     * @return
     */
    List<InspectionUnitsPO> selectUnitsMember(Long unitsId);
}
