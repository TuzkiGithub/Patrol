package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionConsultInfoDetailPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 查阅资料详情Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionConsultInfoDetailMapper extends BaseMapper<InspectionConsultInfoDetailPO> {

    /**
     * 批量新增
     * @param consultInfoDetailList
     * @return
     */
    int insertBatch(List<InspectionConsultInfoDetailPO> consultInfoDetailList);
}
