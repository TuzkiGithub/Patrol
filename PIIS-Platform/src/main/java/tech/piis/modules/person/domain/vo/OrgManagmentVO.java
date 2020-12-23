package tech.piis.modules.person.domain.vo;

import lombok.Data;
import tech.piis.modules.person.domain.po.InspectionOfficePO;
import tech.piis.modules.person.domain.po.LeadingGroupFoundPO;
import tech.piis.modules.person.domain.po.MemberResumePO;
import tech.piis.modules.person.domain.po.OrganizationPO;

import java.util.List;

/**
 * ClassName : OrgManagmentVO
 * Package : tech.piis.modules.person.domain.vo
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
public class OrgManagmentVO {

    private OrganizationPO organizationPO;

    private LeadingGroupFoundPO leadingGroupFoundPO;

    private List<MemberResumePO> leadingGroupMemberList;

    private InspectionOfficePO inspectionOfficePO;

    private List<MemberResumePO> inspectionOfficeMemberList;

    private List<GroupMemberVO> groupMemberVOS;
}
