package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingDetailPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 下沉了解详情Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionSinkingUnderstandingDetailMapper extends BaseMapper<InspectionSinkingUnderstandingDetailPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstandingDetail次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionSinkingUnderstandingDetailCount(String planId) throws BaseException;
}
