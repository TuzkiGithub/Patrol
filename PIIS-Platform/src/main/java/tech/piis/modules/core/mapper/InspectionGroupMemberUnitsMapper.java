package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionGroupMemberUnitsPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.mapper
 * User: Tuzki
 * Date: 2020/10/12
 * Time: 17:02
 * Description:
 */
public interface InspectionGroupMemberUnitsMapper extends BaseMapper<InspectionGroupMemberUnitsPO> {


    /**
     * 批量新增巡视组组员-被巡视单位关系
     * @param groupMemberUnitsList
     * @return
     */
    int insertGroupMemberUnitsBatch(List<InspectionGroupMemberUnitsPO> groupMemberUnitsList);

}
