package tech.piis.modules.core.service;


import tech.piis.modules.core.domain.po.InspectionMobilizeAttendeePO;

import java.util.List;

/**
 * 巡视动员参会人员 Service接口
 * 
 * @author Kevin
 * @date 2020-09-27
 */
public interface IInspectionMobilizeAttendeeService {

    /**
     * 查询动员信息及文件
     * @param mobilizeAttendeeId
     * @return
     */
    List<InspectionMobilizeAttendeePO> selectAttendeeDocuments(String mobilizeAttendeeId);
}