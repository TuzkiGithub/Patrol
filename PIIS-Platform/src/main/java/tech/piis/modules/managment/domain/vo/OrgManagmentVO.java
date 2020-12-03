package tech.piis.modules.managment.domain.vo;

import lombok.Data;
import tech.piis.modules.managment.domain.po.InspectionOfficePO;
import tech.piis.modules.managment.domain.po.LeadingGroupFoundPO;
import tech.piis.modules.managment.domain.po.MemberResumePO;
import tech.piis.modules.managment.domain.po.OrganizationPO;

import java.util.List;

/**
 * ClassName : OrgManagmentVO
 * Package : tech.piis.modules.managment.domain.vo
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
