package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.person.domain.po.LeadingGroupFoundPO;

import java.util.List;

/**
 * ClassName : LeadingGroupFoundMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface LeadingGroupFoundMapper extends BaseMapper<LeadingGroupFoundPO> {

    List<LeadingGroupFoundPO> selectMemberCount(String orgId);
}
