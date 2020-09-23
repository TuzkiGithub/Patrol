package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionGroupPO;

import java.util.List;

/**
 * 巡视组信息 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
public interface InspectionGroupMapper extends BaseMapper<InspectionGroupPO> {
    
    public int insertBatch(List<InspectionGroupPO> groupList);
}
