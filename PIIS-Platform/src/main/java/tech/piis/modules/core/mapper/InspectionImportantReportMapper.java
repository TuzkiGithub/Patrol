package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionImportantReportPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 重要情况专题报告 Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-23
 */
public interface InspectionImportantReportMapper extends BaseMapper<InspectionImportantReportPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionImportantReport次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionImportantReportCount(String planId) throws BaseException;
}
