package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.mapper.InspectionGroupMapper;
import tech.piis.modules.core.service.IInspectionGroupService;

/**
 * 巡视组信息 Service业务层处理
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@Service
public class InspectionGroupServiceImpl implements IInspectionGroupService {
    @Autowired
    private InspectionGroupMapper inspectionGroupMapper;


}
