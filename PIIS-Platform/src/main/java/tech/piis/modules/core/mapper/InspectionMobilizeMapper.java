package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionMobilizePO;

import java.util.List;

/**
 * 巡视动员 Mapper接口
 *
 * @author Kevin
 * @date 2020-09-17
 */
public interface InspectionMobilizeMapper extends BaseMapper<InspectionMobilizePO> {

    /**
     * 查询巡视动员
     * @return
     */
    InspectionMobilizePO selectMobilizeList(InspectionMobilizePO inspectionMobilizePO);
}