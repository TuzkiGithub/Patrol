package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPersonPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 调研走访人员Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionInvestigationVisitPersonMapper extends BaseMapper<InspectionInvestigationVisitPersonPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionInvestigationVisitPerson次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionInvestigationVisitPersonCount(String planId) throws BaseException;
}
