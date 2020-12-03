package tech.piis.modules.managment.service;

import tech.piis.modules.managment.domain.po.MemberResumePO;
import tech.piis.modules.managment.domain.vo.GroupMemberVO;
import tech.piis.modules.managment.domain.vo.OrgManagmentVO;
import tech.piis.modules.managment.domain.vo.OrganizationVO;
import tech.piis.modules.managment.domain.vo.SysDeptVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName : OrganizationService
 * Package : tech.piis.modules.managment.service
 * Description :
 *  机构 service
 * @author : chenhui@xvco.com
 */

public interface IOrganizationService {


    /**
     * 查询人员基础情况
     * @param memberId
     * @return
     */
    Map<String,Object> selectBasicInfoByMemberId(String memberId);

    /**
     * 新增机构
     * @param orgManagmentVO
     * @return
     */
    int save(OrgManagmentVO orgManagmentVO);
    /**
     * 根据机构编号查询机构巡察办成立信息、巡察办成员信息、巡察领导小组成立信息、巡察领导小组成员信息、所属子公司相关信息
     * 、人员履历信息
     */
    OrgManagmentVO selectWholeInfoByOrgId(String orgId);

    /**
     * 修改机构信息
     * @param orgManagmentVO
     * @return
     */
    int update(OrgManagmentVO orgManagmentVO);

    /**
     * 删除机构
     * @param orgId
     * @return
     */
    int delOrganById(String orgId);

    /**
     *
     * @param orgName
     * @return
     */
    List<OrganizationVO> selectListByOrgName(String orgName);

    /**
     * 根据机构编号ID查询直属下级机构
     * @param orgId
     * @return
     */
    List<SysDeptVO> selectChildrenDeptByParentId(String orgId);

    /**
     * 根据人员编号查询人员履历信息以及人员基本情况
     * @param memberId
     * @return
     */
    MemberResumePO selectMemberResumeByMemberId(String memberId);

    GroupMemberVO selectSonLeadingGroupInfo(String orgId);
}
