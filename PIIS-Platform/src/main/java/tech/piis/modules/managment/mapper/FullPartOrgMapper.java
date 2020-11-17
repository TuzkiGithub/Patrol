package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.FullPartOrgPO;

/**
 * ClassName : FullPartOrgMapper
 * Package : tech.piis.modules.managment.mapper
 * Description :
 *  专兼职管理 Mapper
 * @author : chenhui@xvco.com
 */
public interface FullPartOrgMapper extends BaseMapper<FullPartOrgPO> {
    FullPartOrgPO getFullPartNumByOrganId(String organId);
}
