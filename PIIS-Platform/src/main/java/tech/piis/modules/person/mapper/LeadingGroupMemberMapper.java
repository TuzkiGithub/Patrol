package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.person.domain.po.LeadingGroupFoundPO;
import tech.piis.modules.person.domain.po.LeadingGroupMemberPO;
import tech.piis.modules.person.domain.vo.InspectionInfoVO;

import java.util.List;

/**
 * ClassName : LeadingGroupMemberMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface LeadingGroupMemberMapper extends BaseMapper<LeadingGroupMemberPO> {

    List<InspectionInfoVO> queryLeadingInspectionInfo();

    List<LeadingGroupFoundPO> selectMemberCount(String orgId);
}
