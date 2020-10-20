package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 调研走访Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionInvestigationVisitMapper extends BaseMapper<InspectionInvestigationVisitPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionInvestigationVisit次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionInvestigationVisitCount(String planId) throws BaseException;
}
