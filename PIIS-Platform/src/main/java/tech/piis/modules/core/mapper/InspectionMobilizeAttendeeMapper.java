package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionMobilizeAttendeePO;

import java.util.List;

/**
 * 巡视动员参会人员 Mapper接口
 *
 * @author Kevin
 * @date 2020-09-17
 */
public interface InspectionMobilizeAttendeeMapper extends BaseMapper<InspectionMobilizeAttendeePO> {

    /**
     * 批量新增动员人员信息
     * @param inspectionMobilizeAttendeeList
     * @return
     * @throws Exception
     */
    int saveMobilizeAttendeeBatch(List<InspectionMobilizeAttendeePO> inspectionMobilizeAttendeeList) throws Exception;
}