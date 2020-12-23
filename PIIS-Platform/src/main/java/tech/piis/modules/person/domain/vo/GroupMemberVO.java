package tech.piis.modules.person.domain.vo;

import lombok.Data;
import tech.piis.modules.person.domain.po.LeadingGroupFoundPO;
import tech.piis.modules.person.domain.po.MemberResumePO;

import java.util.List;

/**
 * ClassName : GroupMemberVO
 * Package : tech.piis.modules.person.domain.vo
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
public class GroupMemberVO {

    private LeadingGroupFoundPO leadingGroupFoundPO;

    private List<MemberResumePO> memberResumePOS;

    private Integer operationType;
}
