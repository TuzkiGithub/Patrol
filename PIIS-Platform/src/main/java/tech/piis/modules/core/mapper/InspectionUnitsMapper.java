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

    /**
     * 根据被巡视单位ID查询被巡视单位下的组员信息
     *
     * @param unitsId
     * @return
     */
    public List<InspectionUnitsPO> selectUnitsMember(Long unitsId);

}
