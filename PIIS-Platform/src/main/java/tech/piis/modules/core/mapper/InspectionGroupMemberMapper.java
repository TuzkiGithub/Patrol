package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionGroupMemberPO;

import java.util.List;

/**
 * 巡视组组员 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
public interface InspectionGroupMemberMapper extends BaseMapper<InspectionGroupMemberPO> {
    public int insertBatch(List<InspectionGroupMemberPO> groupMemberList);

}
