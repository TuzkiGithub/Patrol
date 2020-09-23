package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.mapper.InspectionGroupMemberMapper;
import tech.piis.modules.core.service.IInspectionGroupMemberService;

/**
 * 巡视组组员 Service业务层处理
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@Service
public class InspectionGroupMemberServiceImpl implements IInspectionGroupMemberService {
    @Autowired
    private InspectionGroupMemberMapper inspectionGroupMemberMapper;


}
