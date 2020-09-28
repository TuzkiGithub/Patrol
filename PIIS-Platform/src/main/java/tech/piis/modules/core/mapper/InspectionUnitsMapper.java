package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionGroupMemberPO;
import tech.piis.modules.core.domain.po.InspectionUnitsPO;

import java.util.List;

/**
 * 被巡视单位 Mapper接口
 *
 * @author Tuzki
 * @date 2020-09-14
 */
public interface InspectionUnitsMapper extends BaseMapper<InspectionUnitsPO> {

    public int insertBatch(List<InspectionUnitsPO> unitsList);

}
