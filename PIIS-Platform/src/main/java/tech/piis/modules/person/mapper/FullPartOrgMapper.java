package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.person.domain.po.FullPartOrgPO;

/**
 * ClassName : FullPartOrgMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *  专兼职管理 Mapper
 * @author : chenhui@xvco.com
 */
public interface FullPartOrgMapper extends BaseMapper<FullPartOrgPO> {
    FullPartOrgPO getFullPartNumByOrganId(String organId);
}
