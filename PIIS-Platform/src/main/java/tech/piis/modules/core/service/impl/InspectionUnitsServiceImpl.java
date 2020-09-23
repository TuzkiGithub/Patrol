package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.mapper.InspectionUnitsMapper;
import tech.piis.modules.core.service.IInspectionUnitsService;

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


}
