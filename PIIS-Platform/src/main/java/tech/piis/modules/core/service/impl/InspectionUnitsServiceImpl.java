package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.domain.po.InspectionUnitsPO;
import tech.piis.modules.core.mapper.InspectionUnitsMapper;
import tech.piis.modules.core.service.IInspectionUnitsService;

import java.util.List;

/**
 * 被巡视单位 Service业务层处理
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@Service
public class InspectionUnitsServiceImpl implements IInspectionUnitsService {
    @Autowired
    private InspectionUnitsMapper inspectionUnitsMapper;


    /**
     * 根据被巡视单位ID查询被巡视单位下的组员信息
     *
     * @param unitsId
     * @return
     */
    @Override
    public InspectionUnitsPO selectUnitsMember(Long unitsId) {
        return inspectionUnitsMapper.selectUnitsMember(unitsId);
    }
}
