package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.RotationExchangePO;

import java.util.List;

/**
 * ClassName : RotationExchangeMapper
 * Package : tech.piis.modules.managment.mapper
 * Description :
 *  轮岗交流service
 * @author : chenhui@xvco.com
 */

public interface RotationExchangeMapper extends BaseMapper<RotationExchangePO> {
    /**
     * 轮岗交流总览
     * @return
     */
    List<RotationExchangePO> selectRecommendListByOrgId();
}
