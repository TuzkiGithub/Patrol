package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.domain.po.InspectionMobilizeAttendeePO;
import tech.piis.modules.core.mapper.InspectionMobilizeAttendeeMapper;
import tech.piis.modules.core.service.IInspectionMobilizeAttendeeService;

import java.util.List;

/**
 * 巡视动员参会人员 Service业务层处理
 * 
 * @author Kevin
 * @date 2020-09-27
 */
@Service
public class InspectionMobilizeAttendeeServiceImpl implements IInspectionMobilizeAttendeeService {
    @Autowired
    private InspectionMobilizeAttendeeMapper inspectionMobilizeAttendeeMapper;


    /**
     * 查询动员信息及文件
     *
     * @param mobilizeAttendeeId
     * @return
     */
    @Override
    public List<InspectionMobilizeAttendeePO> selectAttendeeDocuments(String mobilizeAttendeeId) {
        return inspectionMobilizeAttendeeMapper.selectAttendeeDocuments(mobilizeAttendeeId);
    }
}