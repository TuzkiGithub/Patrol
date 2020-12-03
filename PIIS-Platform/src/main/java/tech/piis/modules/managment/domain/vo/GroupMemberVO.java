package tech.piis.modules.managment.domain.vo;

import lombok.Data;
import tech.piis.modules.managment.domain.po.LeadingGroupFoundPO;
import tech.piis.modules.managment.domain.po.MemberResumePO;

import java.util.List;

/**
 * ClassName : GroupMemberVO
 * Package : tech.piis.modules.managment.domain.vo
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
