package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionLegislationReformPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 立行立改 Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-23
 */
public interface InspectionLegislationReformMapper extends BaseMapper<InspectionLegislationReformPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionLegislationReform次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionLegislationReformCount(String planId) throws BaseException;
}
