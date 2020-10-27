package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionClueTransferDetailPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 线索移交详情 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-10-27
 */
public interface InspectionClueTransferDetailMapper extends BaseMapper<InspectionClueTransferDetailPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionClueTransferDetail次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionClueTransferDetailCount(String planId) throws BaseException;
}
