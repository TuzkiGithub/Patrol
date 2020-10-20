package tech.piis.modules.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionSpecialReportPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

import java.util.List;

/**
 * SPECIAL_REPORT Mapper接口
 *
 * @author Kevin
 * @date 2020-10-12
 */
public interface InspectionSpecialReportMapper extends BaseMapper<InspectionSpecialReportPO> {

    /**
     * 统计巡视方案下被巡视单位的听取报告次数
     *
     * @param planId
     * @return
     */
    List<UnitsBizCountVO> selectSpecialReportCount(String planId);

}
