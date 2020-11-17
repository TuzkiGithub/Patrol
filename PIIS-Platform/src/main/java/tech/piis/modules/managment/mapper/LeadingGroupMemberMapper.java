package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.LeadingGroupMemberPO;
import tech.piis.modules.managment.domain.vo.InspectionInfoVO;

import java.util.List;

/**
 * ClassName : LeadingGroupMemberMapper
 * Package : tech.piis.modules.managment.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface LeadingGroupMemberMapper extends BaseMapper<LeadingGroupMemberPO> {

    List<InspectionInfoVO> queryLeadingInspectionInfo();
}
