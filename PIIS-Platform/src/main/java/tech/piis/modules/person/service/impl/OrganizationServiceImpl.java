package tech.piis.modules.person.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanConditionVO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.person.domain.po.*;
import tech.piis.modules.person.domain.vo.*;
import tech.piis.modules.person.mapper.*;
import tech.piis.modules.person.service.IOrganizationService;
import tech.piis.modules.system.domain.SysDept;
import tech.piis.modules.system.mapper.SysDeptMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * ClassName : IorganizationServiceImpl
 * Package : tech.piis.modules.person.service.impl
 * Description :
 * 机构  service业务层处理
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class OrganizationServiceImpl implements IOrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private MemberResumeMapper memberResumeMapper;
    @Autowired
    private MemberResumeFamilyMapper memberResumeFamilyMapper;
    @Autowired
    private DailyTrainingMapper dailyTrainingMapper;
    @Autowired
    private IInspectionPlanService iInspectionPlanService;
    @Autowired
    private InspectionOfficeMapper inspectionOfficeMapper;
    @Autowired
    private InspectionOfficeMemberMapper inspectionOfficeMemberMapper;
    @Autowired
    private LeadingGroupFoundMapper leadingGroupFoundMapper;
    @Autowired
    private LeadingGroupMemberMapper leadingGroupMemberMapper;
    @Autowired
    private PiisDocumentMapper documentMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    @Override
    public Map<String, Object> selectBasicInfoByMemberId(String memberId) throws BaseException {
        Map<String, Object> resultMap = new HashMap<>();

        // 查询人员家庭情况
        QueryWrapper<MemberResumeFamilyPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", memberId);
        List<MemberResumeFamilyPO> memberResumeFamilyPOS = memberResumeFamilyMapper.selectList(queryWrapper);
        resultMap.put("memberResumeFamilyList", memberResumeFamilyPOS);

        // 查询专业培训情况，确定培训地点
        List<DailyTrainingVO> dailyTrainingPOList = dailyTrainingMapper.selectDailyTrainingInfo(memberId);
        resultMap.put("dailyTrainingList", dailyTrainingPOList);

        // 查询人员参与巡视巡察情况
        List<PlanConditionVO> planConditionVOS = iInspectionPlanService.selectPiisConditionByUserId(memberId);
        if (!CollectionUtils.isEmpty(planConditionVOS)) {
            planConditionVOS.forEach(planConditionVO -> {
                String groupPost = null;
                if (StringUtils.isNotBlank(planConditionVO.getConnectorPost())) {
                    groupPost = planConditionVO.getConnectorPost();
                } else if (StringUtils.isNotBlank(planConditionVO.getDeputyPost())) {
                    groupPost = planConditionVO.getDeputyPost();
                } else if (StringUtils.isNotBlank(planConditionVO.getLeaderPost())) {
                    groupPost = planConditionVO.getLeaderPost();
                } else {
                    groupPost = planConditionVO.getMemberPost();
                }
                planConditionVO.setGroupPost(groupPost);
            });
        }
        resultMap.put("planConditionVOList", planConditionVOS);
        return resultMap;
    }

    /**
     * 新增机构
     *
     * @param orgManagmentVO
     * @return
     */
    @Override
    public int save(OrgManagmentVO orgManagmentVO) throws BaseException {
        // 新增巡察办成立表
        InspectionOfficePO inspectionOfficePO = orgManagmentVO.getInspectionOfficePO();
        String inspectionOfficeId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionOfficePO.setInspectionOfficeId(inspectionOfficeId);
        inspectionOfficePO.setOrgId(orgManagmentVO.getOrganizationPO().getOrgId());

        BizUtils.setCreatedOperation(InspectionOfficePO.class, inspectionOfficePO);
        BizUtils.setCreatedTimeOperation(InspectionOfficePO.class, inspectionOfficePO);
        inspectionOfficeMapper.insert(inspectionOfficePO);
        List<PiisDocumentPO> officeFoundDocList = inspectionOfficePO.getOfficeFoundDocList();
        bundledFile(officeFoundDocList, inspectionOfficeId);
        // 更新文件表
//        updateFile(inspectionOfficeId);
        // 新增巡察办成员表
        List<MemberResumePO> memberResumePOList = orgManagmentVO.getInspectionOfficeMemberList();
        memberResumePOList.forEach(memberResumePO -> {
            InspectionOfficeMemberPO officeMemberPO = new InspectionOfficeMemberPO();
            String inspectionMemberId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
            officeMemberPO.setInspectionMemberId(inspectionMemberId);
            officeMemberPO.setOrgId(orgManagmentVO.getOrganizationPO().getOrgId());
            officeMemberPO.setInspectionOfficeId(inspectionOfficeId);
            officeMemberPO.setMemberId(memberResumePO.getMemberId());
            officeMemberPO.setMemberName(memberResumePO.getName());
            officeMemberPO.setMemberPost(memberResumePO.getPost());
            officeMemberPO.setMemberRole(memberResumePO.getRole());
            // 新增巡察办成员表
            BizUtils.setCreatedOperation(InspectionOfficeMemberPO.class, officeMemberPO);
            BizUtils.setCreatedTimeOperation(InspectionOfficeMemberPO.class, officeMemberPO);
            inspectionOfficeMemberMapper.insert(officeMemberPO);
            List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
            bundledFile(appointDocuPOs, inspectionMemberId);

            MemberResumePO memberResumePO1 = memberResumeMapper.selectById(memberResumePO.getMemberId());
            if (null == memberResumePO1) {
                // 新增人员履历表
                memberResumePO1 = new MemberResumePO();
                BizUtils.setCreatedOperation(MemberResumePO.class, memberResumePO1);
                memberResumeMapper.insert(memberResumePO);
            }
        });

        // 新增巡察领导小组成立表
        LeadingGroupFoundPO leadingGroupFoundPO = orgManagmentVO.getLeadingGroupFoundPO();
        List<MemberResumePO> memberResumePOS = orgManagmentVO.getLeadingGroupMemberList();

        saveGroupAndMember(orgManagmentVO, leadingGroupFoundPO, memberResumePOS);

        // 新增子公司领导小组成立表
        List<GroupMemberVO> groupMemberVOS = orgManagmentVO.getGroupMemberVOS();
        // 新增领导小组以及领导小组成员
        groupMemberVOS.forEach(this::saveSonGroupAndMember);
        // 新增巡视/巡查机构
        OrganizationPO organizationPO = orgManagmentVO.getOrganizationPO();
        SysDept dept = sysDeptMapper.selectDeptById(organizationPO.getOrgId());
        organizationPO.setPid(dept.getParentId());
        return organizationMapper.insert(organizationPO);
    }

    private void saveSonGroupAndMember(GroupMemberVO groupMemberVO) {
        String leadingGroupId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        groupMemberVO.getLeadingGroupFoundPO().setLeadingGroupId(leadingGroupId);
        bundledFile(groupMemberVO.getLeadingGroupFoundPO().getGroupFoundDocList(), leadingGroupId);
        leadingGroupFoundMapper.insert(groupMemberVO.getLeadingGroupFoundPO());
        List<MemberResumePO> memberResumePOS = groupMemberVO.getMemberResumePOS();
        memberResumePOS.forEach(memberResumePO -> {
            LeadingGroupMemberPO groupMemberPO = new LeadingGroupMemberPO();
            String leadingMemberId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
            groupMemberPO.setLeadingMemberId(leadingMemberId);
            groupMemberPO.setOrgId(groupMemberVO.getLeadingGroupFoundPO().getOrgId());
            groupMemberPO.setMemberId(memberResumePO.getMemberId());
            groupMemberPO.setMemberName(memberResumePO.getName());
            groupMemberPO.setMemberPost(memberResumePO.getPost());
            groupMemberPO.setMemberRole(memberResumePO.getRole());
            List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
            bundledFile(appointDocuPOs, leadingMemberId);
            groupMemberPO.setLeadingGroupId(leadingGroupId);
            leadingGroupMemberMapper.insert(groupMemberPO);

            MemberResumePO memberResumePO1 = memberResumeMapper.selectById(memberResumePO.getMemberId());
            if (null == memberResumePO1) {
                // 新增人员履历表
                memberResumeMapper.insert(memberResumePO);
            }

        });
    }

    /**
     * 根据机构编号查询机构巡察办成立信息、巡察办成员信息、巡察领导小组成立信息、巡察领导小组成员信息、所属子公司相关信息
     * 、人员履历信息
     */
    @Override
    public OrgManagmentVO selectWholeInfoByOrgId(String orgId) throws BaseException {
        OrgManagmentVO orgManagmentVO = new OrgManagmentVO();

        // 查询巡察领导小组成立信息表
        QueryWrapper<LeadingGroupFoundPO> queryFound = new QueryWrapper<>();
        queryFound.eq("org_id", orgId);
        LeadingGroupFoundPO leadingGroupFoundPO = leadingGroupFoundMapper.selectOne(queryFound);
        String leadingGroupId = leadingGroupFoundPO.getLeadingGroupId();
        // 获取成立文件
        List<PiisDocumentPO> documentsByObjectId = getDocumentsByObjectId(leadingGroupId);
        leadingGroupFoundPO.setGroupFoundDocList(documentsByObjectId);
        orgManagmentVO.setLeadingGroupFoundPO(leadingGroupFoundPO);

        QueryWrapper<LeadingGroupMemberPO> queryMember = new QueryWrapper<>();
        queryMember.eq("org_id", orgId);
        // 查询巡察领导小组成员信息表
        List<LeadingGroupMemberPO> leadingGroupMemberPOS = leadingGroupMemberMapper.selectList(queryMember);
        List<MemberResumePO> groupMemberPOS = new ArrayList<>();
        leadingGroupMemberPOS.forEach(leadingGroupMemberPO -> {
            String memberId = leadingGroupMemberPO.getMemberId();

            MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);
            memberResumePO.setRole(leadingGroupMemberPO.getMemberRole());
            memberResumePO.setLeadingMemberId(leadingGroupMemberPO.getLeadingMemberId());
            // 获得成员基本情况信息
            Map<String, Object> memberBasicMap = selectBasicInfoByMemberId(memberId);
            if (!memberBasicMap.isEmpty()) {
                memberResumePO.setMemberBasicMap(memberBasicMap);
            }

            String leadingMemberId = leadingGroupMemberPO.getLeadingMemberId();
            List<PiisDocumentPO> documents = getDocumentsByObjectId(leadingMemberId);
            memberResumePO.setAppointDocuPOs(documents);

            groupMemberPOS.add(memberResumePO);
//            leadingGroupMemberPO.setPiisDocumentPO(documents);
        });
        orgManagmentVO.setLeadingGroupMemberList(groupMemberPOS);

        //查询巡察办成立信息表
        QueryWrapper<InspectionOfficePO> queryOffice = new QueryWrapper<>();
        queryOffice.eq("org_id", orgId);
        InspectionOfficePO inspectionOfficePO = inspectionOfficeMapper.selectOne(queryOffice);
        if (null != inspectionOfficePO) {
            String inspectionOfficeId = inspectionOfficePO.getInspectionOfficeId();
            List<PiisDocumentPO> officeDocuments = getDocumentsByObjectId(inspectionOfficeId);
            inspectionOfficePO.setOfficeFoundDocList(officeDocuments);
            orgManagmentVO.setInspectionOfficePO(inspectionOfficePO);

            // 查询巡察办成员信息表
            QueryWrapper<InspectionOfficeMemberPO> queryOfficeMember = new QueryWrapper<>();
            queryOfficeMember.eq("org_id", orgId);
            List<InspectionOfficeMemberPO> inspectionOfficeMemberPOS = inspectionOfficeMemberMapper.selectList(queryOfficeMember);
            List<MemberResumePO> officeMemberPOS = new ArrayList<>();
            if (!CollectionUtils.isEmpty(inspectionOfficeMemberPOS)) {
                inspectionOfficeMemberPOS.forEach(inspectionOfficeMemberPO -> {
                    String memberId = inspectionOfficeMemberPO.getMemberId();
                    MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);
                    if (null != memberResumePO) {
                        // 获得成员基本情况信息
                        Map<String, Object> memberBasicMap = selectBasicInfoByMemberId(memberId);
                        if (!memberBasicMap.isEmpty()) {
                            memberResumePO.setMemberBasicMap(memberBasicMap);
                        }
                        memberResumePO.setRole(inspectionOfficeMemberPO.getMemberRole());
                        memberResumePO.setLeadingMemberId(inspectionOfficeMemberPO.getInspectionMemberId());
                        String inspectionMemberId = inspectionOfficeMemberPO.getInspectionMemberId();
                        List<PiisDocumentPO> memberDocuments = getDocumentsByObjectId(inspectionMemberId);
                        memberResumePO.setAppointDocuPOs(memberDocuments);
                        officeMemberPOS.add(memberResumePO);
                    }
                });
            }
            orgManagmentVO.setInspectionOfficeMemberList(officeMemberPOS);
            // 查询子公司巡察机构成员数量
//        List<LeadingGroupFoundPO> leadingGroupFoundPOList = leadingGroupMemberMapper.selectMemberCount(orgId);
            List<LeadingGroupFoundPO> leadingGroupFoundPOList = leadingGroupFoundMapper.selectMemberCount(orgId);
            List<GroupMemberVO> groupMemberVOS = new ArrayList<>();
        /*leadingGroupFoundPOList.forEach(leadingGroupFoundPO1->{
            GroupMemberVO groupMemberVO = new GroupMemberVO();
            groupMemberVO.setLeadingGroupFoundPO(leadingGroupFoundPO1);
            groupMemberVOS.add(groupMemberVO);
        });*/

            leadingGroupFoundPOList.forEach(groupFoundPO -> {
                GroupMemberVO groupMemberVO = new GroupMemberVO();
                String sonOrgId = groupFoundPO.getOrgId();
                QueryWrapper<LeadingGroupFoundPO> queryFound1 = new QueryWrapper<>();
                queryFound1.eq("org_id", sonOrgId);
                LeadingGroupFoundPO leadingGroupFoundPO1 = leadingGroupFoundMapper.selectOne(queryFound1);
                String leadingGroupId1 = leadingGroupFoundPO1.getLeadingGroupId();
                leadingGroupFoundPO1.setMemberNum(groupFoundPO.getMemberNum());
                List<PiisDocumentPO> documentsByObjectId1 = getDocumentsByObjectId(leadingGroupId1);
                leadingGroupFoundPO1.setGroupFoundDocList(documentsByObjectId1);
                groupMemberVO.setLeadingGroupFoundPO(leadingGroupFoundPO1);

                QueryWrapper<LeadingGroupMemberPO> queryMemberWrapper = new QueryWrapper<>();
                queryMemberWrapper.eq("org_id", sonOrgId);
                // 查询巡察领导小组成员信息表
                List<LeadingGroupMemberPO> sonLeadingGroupMemberPOS = leadingGroupMemberMapper.selectList(queryMemberWrapper);
                List<MemberResumePO> sonGroupMemberPOS = new ArrayList<>();
                sonLeadingGroupMemberPOS.forEach(leadingGroupMemberPO -> {
                    String memberId = leadingGroupMemberPO.getMemberId();
                    MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);

                    // 获得成员基本情况信息
                    Map<String, Object> memberBasicMap = selectBasicInfoByMemberId(memberId);
                    if (!memberBasicMap.isEmpty()) {
                        memberResumePO.setMemberBasicMap(memberBasicMap);
                    }

                    memberResumePO.setRole(leadingGroupMemberPO.getMemberRole());
                    memberResumePO.setLeadingMemberId(leadingGroupMemberPO.getLeadingMemberId());
                    String leadingMemberId = leadingGroupMemberPO.getLeadingMemberId();
                    List<PiisDocumentPO> documents = getDocumentsByObjectId(leadingMemberId);
                    memberResumePO.setAppointDocuPOs(documents);
                    sonGroupMemberPOS.add(memberResumePO);
//            leadingGroupMemberPO.setPiisDocumentPO(documents);
                });
                groupMemberVO.setMemberResumePOS(sonGroupMemberPOS);
                groupMemberVOS.add(groupMemberVO);
            });


            orgManagmentVO.setGroupMemberVOS(groupMemberVOS);
        }

        // 查询机构详细信息
        OrganizationPO organizationPO = organizationMapper.selectById(orgId);
        orgManagmentVO.setOrganizationPO(organizationPO);
        return orgManagmentVO;
    }

    private List<PiisDocumentPO> getDocumentsByObjectId(String leadingGroupId) {
        QueryWrapper<PiisDocumentPO> docuWrapper = new QueryWrapper<>();
        docuWrapper.eq("object_id", leadingGroupId);
        return documentMapper.selectList(docuWrapper);
    }

    @Override
    public int update(OrgManagmentVO orgManagmentVO) throws BaseException {
        /*delOrganById(orgManagmentVO.getOrganizationPO().getOrgId());
        return save(orgManagmentVO);*/
        LeadingGroupFoundPO leadingGroupFoundPO = orgManagmentVO.getLeadingGroupFoundPO();
        // 更新领导小组成立表
        editGroupFound(leadingGroupFoundPO);

        String leadingGroupId = leadingGroupFoundPO.getLeadingGroupId();
        List<MemberResumePO> leadingGroupMemberList = orgManagmentVO.getLeadingGroupMemberList();
        // 更新领导小组成员表
        editGroupMember(orgManagmentVO, leadingGroupMemberList, leadingGroupId);

        // 更新子公司巡查机构成立信息，以及巡察领导小组成员信息
        editGroupMemberVO(orgManagmentVO);

        // 更新巡察办成立表
        InspectionOfficePO inspectionOfficePO = orgManagmentVO.getInspectionOfficePO();

        String inspectionOfficeId = inspectionOfficePO.getInspectionOfficeId();
        List<PiisDocumentPO> documents = getDocumentsByObjectId(inspectionOfficeId);
        unbundledFile(documents);
        List<PiisDocumentPO> officeFoundDocList = inspectionOfficePO.getOfficeFoundDocList();
        bundledFile(officeFoundDocList, inspectionOfficeId);
        BizUtils.setUpdatedOperation(InspectionOfficePO.class, inspectionOfficePO);
        BizUtils.setUpdatedTimeOperation(InspectionOfficePO.class, inspectionOfficePO);
        inspectionOfficeMapper.updateById(inspectionOfficePO);

        List<MemberResumePO> inspectionOfficeMemberList = orgManagmentVO.getInspectionOfficeMemberList();
        //更新巡察办成员表
        editOfficeMember(orgManagmentVO.getOrganizationPO().getOrgId(), inspectionOfficeMemberList, inspectionOfficePO.getInspectionOfficeId());
        OrganizationPO organizationPO = orgManagmentVO.getOrganizationPO();
        BizUtils.setUpdatedOperation(OrganizationPO.class, organizationPO);
        BizUtils.setUpdatedTimeOperation(OrganizationPO.class, organizationPO);
        return organizationMapper.updateById(organizationPO);
    }

    private void editGroupMemberVO(OrgManagmentVO orgManagmentVO) {
        List<GroupMemberVO> groupMemberVOS = orgManagmentVO.getGroupMemberVOS();
        if (!CollectionUtils.isEmpty(groupMemberVOS)) {
            groupMemberVOS.forEach(groupMemberVO -> {
                        //更新子公司巡查机构
                        leadingGroupFoundMapper.updateById(groupMemberVO.getLeadingGroupFoundPO());
                        // 删除领导小组成员表，删除文件
                        List<MemberResumePO> memberResumePOS = groupMemberVO.getMemberResumePOS();
                        memberResumePOS.forEach(memberResumePO -> {
                            // 删除人员履历表，删除照片
                            String memberId = memberResumePO.getMemberId();
                            MemberResumePO memberResumePO1 = memberResumeMapper.selectById(memberId);
                            if (null != memberResumePO1) {
                                String photo = memberResumePO.getPhoto();
                                FileUploadUtils.deleteServerFile(photo);
                                memberResumeMapper.deleteById(memberResumePO.getMemberId());
                            }
                            List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
                            unbundledFile(appointDocuPOs);
                            leadingGroupMemberMapper.deleteById(memberResumePO.getLeadingMemberId());
                        });


                        //新增
                        List<MemberResumePO> memberResumeList = groupMemberVO.getMemberResumePOS();
                        memberResumeList.forEach(memberResumePO -> {
                            LeadingGroupMemberPO groupMemberPO = new LeadingGroupMemberPO();
                            String leadingMemberId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
                            groupMemberPO.setLeadingMemberId(leadingMemberId);
                            groupMemberPO.setOrgId(groupMemberVO.getLeadingGroupFoundPO().getOrgId());
                            groupMemberPO.setMemberId(memberResumePO.getMemberId());
                            groupMemberPO.setMemberName(memberResumePO.getName());
                            groupMemberPO.setMemberPost(memberResumePO.getPost());
                            groupMemberPO.setMemberRole(memberResumePO.getRole());
                            List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
                            bundledFile(appointDocuPOs, leadingMemberId);
                            groupMemberPO.setLeadingGroupId(groupMemberVO.getLeadingGroupFoundPO().getLeadingGroupId());
                            leadingGroupMemberMapper.insert(groupMemberPO);
                            MemberResumePO memberResumePO1 = memberResumeMapper.selectById(memberResumePO.getMemberId());
                            if (null == memberResumePO1) {
                                // 新增人员履历表
                                memberResumeMapper.insert(memberResumePO);
                            }

                        });
//                Integer operationType = groupMemberVO.getOperationType();
//                if (operationType != null) {
//                    switch (operationType) {
//                        case INSERT: {
//                            saveSonGroupAndMember(groupMemberVO);
//                            break;
//                        }
//                        case UPDATE: {
//                            LeadingGroupFoundPO leadingGroupFoundPO = groupMemberVO.getLeadingGroupFoundPO();
//                            List<MemberResumePO> memberResumePOS = groupMemberVO.getMemberResumePOS();
//                            editGroupMember(orgManagmentVO, memberResumePOS, leadingGroupFoundPO.getLeadingGroupId());
//                            break;
//                        }
//                        case DELETE: {
//                            // 删除领导小组成立表，删除文件
//                            String leadingGroupId = groupMemberVO.getLeadingGroupFoundPO().getLeadingGroupId();
//                            List<PiisDocumentPO> documents = getDocumentsByObjectId(leadingGroupId);
//                            unbundledFile(documents);
//
//                            leadingGroupFoundMapper.deleteById(leadingGroupId);
//                            // 删除领导小组成员表，删除文件
//                            List<MemberResumePO> memberResumePOS = groupMemberVO.getMemberResumePOS();
//                            memberResumePOS.forEach(memberResumePO -> {
//                                // 删除人员履历表，删除照片
//                                String memberId = memberResumePO.getMemberId();
//                                MemberResumePO memberResumePO1 = memberResumeMapper.selectById(memberId);
//                                if (null != memberResumePO1) {
//                                    String photo = memberResumePO.getPhoto();
//                                    FileUploadUtils.deleteServerFile(photo);
//                                    memberResumeMapper.deleteById(memberResumePO.getMemberId());
//                                }
//                                List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
//                                unbundledFile(appointDocuPOs);
//                                String leadingMemberId = memberResumePO.getLeadingMemberId();
//                                leadingGroupMemberMapper.deleteById(leadingMemberId);
//                            });
//                            break;
//                        }
//                    }
//
//                }

                    }
            );
        }
    }

    /**
     * 删除机构
     *
     * @param orgId
     * @return
     */
    @Override
    public int delOrganById(String orgId) throws BaseException {
        // 删除巡视巡察办成立表
        QueryWrapper<InspectionOfficePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_id", orgId);
        InspectionOfficePO inspectionOfficePO = inspectionOfficeMapper.selectOne(queryWrapper);
        String inspectionOfficeId = inspectionOfficePO.getInspectionOfficeId();
        List<PiisDocumentPO> documentsByObjectId = getDocumentsByObjectId(inspectionOfficeId);
        unbundledFile(documentsByObjectId);
        inspectionOfficeMapper.delete(queryWrapper);
        // 删除巡视巡察办成员表
        QueryWrapper<InspectionOfficeMemberPO> query = new QueryWrapper<>();
        query.eq("org_id", orgId);
        List<InspectionOfficeMemberPO> inspectionOfficeMemberPOS = inspectionOfficeMemberMapper.selectList(query);
        inspectionOfficeMemberPOS.forEach(inspectionOfficeMemberPO -> {
            String memberId = inspectionOfficeMemberPO.getMemberId();
            MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);
            if (memberResumePO != null) {
                String photo = memberResumePO.getPhoto();
                FileUploadUtils.deleteServerFile(photo);
                memberResumeMapper.deleteById(memberId);
            }
            String inspectionMemberId = inspectionOfficeMemberPO.getInspectionMemberId();
            List<PiisDocumentPO> documentsByObjectId1 = getDocumentsByObjectId(inspectionMemberId);
            unbundledFile(documentsByObjectId1);
        });
        inspectionOfficeMemberMapper.delete(query);

        // 删除领导小组成立表
        QueryWrapper<LeadingGroupFoundPO> foundPOQueryWrapper = new QueryWrapper<>();
        foundPOQueryWrapper.eq("org_id", orgId);
        LeadingGroupFoundPO leadingGroupFoundPO = leadingGroupFoundMapper.selectOne(foundPOQueryWrapper);
        String leadingGroupId = leadingGroupFoundPO.getLeadingGroupId();
        List<PiisDocumentPO> documentsByObjectId1 = getDocumentsByObjectId(leadingGroupId);
        unbundledFile(documentsByObjectId1);
        leadingGroupFoundMapper.delete(foundPOQueryWrapper);
        // 删除领导小组成员表
        QueryWrapper<LeadingGroupMemberPO> memberPOQueryWrapper = new QueryWrapper<>();
        memberPOQueryWrapper.eq("org_id", orgId);
        List<LeadingGroupMemberPO> leadingGroupMemberPOS = leadingGroupMemberMapper.selectList(memberPOQueryWrapper);
        leadingGroupMemberPOS.forEach(leadingGroupMemberPO -> {
            String memberId = leadingGroupMemberPO.getMemberId();
            delPhotoAndMemberResume(memberId);
            String leadingMemberId = leadingGroupMemberPO.getLeadingMemberId();
            List<PiisDocumentPO> documentsByObjectId2 = getDocumentsByObjectId(leadingMemberId);
            unbundledFile(documentsByObjectId2);
        });
        leadingGroupMemberMapper.delete(memberPOQueryWrapper);

        // 删除直属子机构领导小组成立表
        QueryWrapper<LeadingGroupFoundPO> sonLeadingFoundWrapper = new QueryWrapper<>();
        sonLeadingFoundWrapper.eq("father_org_id", orgId);
        List<LeadingGroupFoundPO> leadingGroupFoundPOS = leadingGroupFoundMapper.selectList(sonLeadingFoundWrapper);
        leadingGroupFoundPOS.forEach(sonLeadingGroupFoundPO -> {
            // 删除领导小组成立文件
            String leadingGroupId1 = sonLeadingGroupFoundPO.getLeadingGroupId();
            List<PiisDocumentPO> documentsByObjectId2 = getDocumentsByObjectId(leadingGroupId1);
            unbundledFile(documentsByObjectId2);
            String sonOrgId = sonLeadingGroupFoundPO.getOrgId();
            QueryWrapper<LeadingGroupMemberPO> sonMemberPOWrapper = new QueryWrapper<>();
            sonMemberPOWrapper.eq("org_id", sonOrgId);
            List<LeadingGroupMemberPO> sonLeadingGroupMemberPOS = leadingGroupMemberMapper.selectList(sonMemberPOWrapper);
            for (LeadingGroupMemberPO sonLeadingGroupMemberPO : sonLeadingGroupMemberPOS) {
                String leadingMemberId = sonLeadingGroupMemberPO.getLeadingMemberId();
                String memberId = sonLeadingGroupMemberPO.getMemberId();
                delPhotoAndMemberResume(memberId);
                List<PiisDocumentPO> documentsByObjectId3 = getDocumentsByObjectId(leadingMemberId);
                unbundledFile(documentsByObjectId3);
            }
            // 删除小组成员
            leadingGroupMemberMapper.delete(sonMemberPOWrapper);

        });
        leadingGroupFoundMapper.delete(sonLeadingFoundWrapper);
        // 删除机构表中巡察机构
        return organizationMapper.deleteById(orgId);
    }

    private void delPhotoAndMemberResume(String memberId) {
        if (StringUtils.isNotBlank(memberId)) {
            MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);
            if (memberResumePO != null) {
                String photo = memberResumePO.getPhoto();
                if (!StringUtils.isEmpty(photo)) {
                    FileUploadUtils.deleteServerFile(photo.replace(serverAddr + "/photo", baseFileUrl));
                }
                memberResumeMapper.deleteById(memberId);
            }
        }
    }


    @Override
    public List<OrganizationVO> selectListByOrgName(String orgName) throws BaseException {
        return organizationMapper.selectListByOrgName(orgName);
    }


    @Override
    public List<SysDeptVO> selectChildrenDeptByParentId(String orgId) throws BaseException {
        List<SysDeptVO> list = new ArrayList<>();
        List<SysDept> sysDeptList = sysDeptMapper.selectChildrenDeptByParentId(orgId);
        sysDeptList.forEach(sysDept -> {
            SysDeptVO sysDeptVO = new SysDeptVO();
            sysDeptVO.setDeptId(sysDept.getDeptId());
            sysDeptVO.setDeptName(sysDept.getDeptName());
            list.add(sysDeptVO);
        });
        return list;
    }

    @Override
    public MemberResumePO selectMemberResumeByMemberId(String memberId) throws BaseException {
        MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);
        Map<String, Object> stringObjectMap = selectBasicInfoByMemberId(memberId);
        if (!stringObjectMap.isEmpty()) {
            memberResumePO.setMemberBasicMap(stringObjectMap);
        }
        return memberResumePO;
    }

    @Override
    public GroupMemberVO selectSonLeadingGroupInfo(String orgId) throws BaseException {
        GroupMemberVO groupMemberVO = new GroupMemberVO();

        QueryWrapper<LeadingGroupFoundPO> queryFound = new QueryWrapper<>();
        queryFound.eq("org_id", orgId);
        LeadingGroupFoundPO leadingGroupFoundPO = leadingGroupFoundMapper.selectOne(queryFound);
        groupMemberVO.setLeadingGroupFoundPO(leadingGroupFoundPO);

        List<MemberResumePO> memberResumePOS = new ArrayList<>();
        QueryWrapper<LeadingGroupMemberPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_id", orgId);
        List<LeadingGroupMemberPO> leadingGroupMemberPOS = leadingGroupMemberMapper.selectList(queryWrapper);
        leadingGroupMemberPOS.forEach(leadingGroupMemberPO -> {
            MemberResumePO memberResumePO = new MemberResumePO();
            memberResumePO.setRole(leadingGroupMemberPO.getMemberRole());
            List<PiisDocumentPO> documents = getDocumentsByObjectId(leadingGroupMemberPO.getLeadingMemberId());
            memberResumePO.setAppointDocuPOs(documents);
            memberResumePOS.add(memberResumePO);
        });
        groupMemberVO.setMemberResumePOS(memberResumePOS);
        return groupMemberVO;

        /*queryFound.eq("org_id", orgId);
        LeadingGroupFoundPO leadingGroupFoundPO = leadingGroupFoundMapper.selectOne(queryFound);
        String leadingGroupId1 = leadingGroupFoundPO.getLeadingGroupId();
        List<PiisDocumentPO> documentsByObjectId1 = getDocumentsByObjectId(leadingGroupId1);
        leadingGroupFoundPO.setGroupFoundDocList(documentsByObjectId1);
        groupMemberVO.setLeadingGroupFoundPO(leadingGroupFoundPO);

        QueryWrapper<LeadingGroupMemberPO> queryMemberWrapper = new QueryWrapper<>();
        queryMemberWrapper.eq("org_id", orgId);
        // 查询巡察领导小组成员信息表
        List<LeadingGroupMemberPO> sonLeadingGroupMemberPOS = leadingGroupMemberMapper.selectList(queryMemberWrapper);
        List<MemberResumePO> sonGroupMemberPOS = new ArrayList<>();
        sonLeadingGroupMemberPOS.forEach(leadingGroupMemberPO -> {
            String memberId = leadingGroupMemberPO.getMemberId();
            MemberResumePO memberResumePO = memberResumeMapper.selectById(memberId);

            // 获得成员基本情况信息
//                Map<String, Object> memberBasicMap = selectBasicInfoByMemberId(memberId);
//                memberResumePO.setMemberBasicMap(memberBasicMap);

            memberResumePO.setRole(leadingGroupMemberPO.getMemberRole());
            String leadingMemberId = leadingGroupMemberPO.getLeadingMemberId();
            List<PiisDocumentPO> documents = getDocumentsByObjectId(leadingMemberId);
            memberResumePO.setAppointDocuPOs(documents);
            sonGroupMemberPOS.add(memberResumePO);
//            leadingGroupMemberPO.setPiisDocumentPO(documents);
        });
        groupMemberVO.setMemberResumePOS(sonGroupMemberPOS);*/


    }

    /**
     * 跟新领导小组成立信息
     *
     * @param leadingGroupFoundPO
     */
    private void editGroupFound(LeadingGroupFoundPO leadingGroupFoundPO) {
        String leadingGroupId = leadingGroupFoundPO.getLeadingGroupId();
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", leadingGroupId);
        List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(queryWrapper);
        unbundledFile(piisDocumentPOS);
        // 更新领导小组成立信息
        List<PiisDocumentPO> groupFoundDocList = leadingGroupFoundPO.getGroupFoundDocList();
        bundledFile(groupFoundDocList, leadingGroupId);
        BizUtils.setUpdatedOperation(LeadingGroupFoundPO.class, leadingGroupFoundPO);
        BizUtils.setUpdatedTimeOperation(LeadingGroupFoundPO.class, leadingGroupFoundPO);
        leadingGroupFoundMapper.updateById(leadingGroupFoundPO);
    }

    private void editOfficeMember(String orgId,
                                  List<MemberResumePO> inspectionOfficeMemberList,
                                  String inspectionOfficeId) {
        inspectionOfficeMemberList.forEach(memberResumePO -> {
            Integer operationType = memberResumePO.getOperationType();
            if (operationType != null) {
                switch (operationType) {
                    case INSERT: {
                        // 新增成员履历表
                        memberResumeMapper.insert(memberResumePO);
                        // 新增领导小组成员表
                        InspectionOfficeMemberPO inspectionOfficeMemberPO = new InspectionOfficeMemberPO();
                        String inspectionMemberId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
                        inspectionOfficeMemberPO.setInspectionMemberId(inspectionMemberId);
                        inspectionOfficeMemberPO.setInspectionOfficeId(inspectionOfficeId);
                        inspectionOfficeMemberPO.setMemberRole(memberResumePO.getRole());
                        inspectionOfficeMemberPO.setMemberPost(memberResumePO.getPost());
                        inspectionOfficeMemberPO.setMemberName(memberResumePO.getName());
                        inspectionOfficeMemberPO.setOrgId(orgId);
                        inspectionOfficeMemberMapper.insert(inspectionOfficeMemberPO);
                        // 任命文件表业务字段
                        List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
                        bundledFile(appointDocuPOs, inspectionMemberId);
                        break;
                    }
                    case UPDATE: {
                        //更新人员任命文件
                        String inspectionMemberId = memberResumePO.getInspectionMemberId();
                        List<PiisDocumentPO> documents = getDocumentsByObjectId(inspectionMemberId);
                        unbundledFile(documents);
                        List<PiisDocumentPO> appointDocuPOs1 = memberResumePO.getAppointDocuPOs();
                        bundledFile(appointDocuPOs1, inspectionMemberId);
                        //更新人员履历
                        BizUtils.setUpdatedOperation(MemberResumePO.class, memberResumePO);
                        BizUtils.setUpdatedTimeOperation(MemberResumePO.class, memberResumePO);
                        memberResumeMapper.updateById(memberResumePO);
                        //更新领导小组成员表
                        InspectionOfficeMemberPO inspectionOfficeMemberPO = new InspectionOfficeMemberPO();
                        inspectionOfficeMemberPO.setInspectionOfficeId(inspectionOfficeId);
                        inspectionOfficeMemberPO.setMemberRole(memberResumePO.getRole());
                        inspectionOfficeMemberPO.setMemberPost(memberResumePO.getPost());
                        inspectionOfficeMemberPO.setMemberName(memberResumePO.getName());
                        inspectionOfficeMemberPO.setOrgId(orgId);
                        BizUtils.setUpdatedOperation(InspectionOfficeMemberPO.class, inspectionOfficeMemberPO);
                        BizUtils.setUpdatedTimeOperation(InspectionOfficeMemberPO.class, inspectionOfficeMemberPO);
                        inspectionOfficeMemberMapper.updateById(inspectionOfficeMemberPO);
                        break;

                    }
                    case DELETE: {
                        // 删除任命文件
                        String inspectionMemberId = memberResumePO.getInspectionMemberId();
                        List<PiisDocumentPO> documents = getDocumentsByObjectId(inspectionMemberId);
                        unbundledFile(documents);
                        String memberId = memberResumePO.getMemberId();
                        // 删除成员履历表
                        delPhotoAndMemberResume(memberId);

                        // 删除领导小组成员表记录
                        QueryWrapper<InspectionOfficeMemberPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("inspection_member_id", inspectionMemberId);
                        inspectionOfficeMemberMapper.delete(queryWrapper);
                        break;
                    }
                }

            } else {
                log.warn("memberResumePO operationType is null! memberResumePO = {}", memberResumePO);
            }
        });
    }

    private void editGroupMember(OrgManagmentVO orgManagmentVO,
                                 List<MemberResumePO> memberResumePOList,
                                 String leadingGroupId) {
        memberResumePOList.forEach(memberResumePO -> {
            Integer operationType = memberResumePO.getOperationType();
            if (operationType != null) {
                switch (operationType) {
                    case INSERT: {
                        // 新增成员履历表
                        memberResumeMapper.insert(memberResumePO);
                        // 新增领导小组成员表
                        LeadingGroupMemberPO leadingGroupMemberPO = new LeadingGroupMemberPO();
                        String leadingMemberId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
                        leadingGroupMemberPO.setLeadingMemberId(leadingMemberId);
                        leadingGroupMemberPO.setLeadingGroupId(leadingGroupId);
                        leadingGroupMemberPO.setMemberRole(memberResumePO.getRole());
                        leadingGroupMemberPO.setMemberPost(memberResumePO.getPost());
                        leadingGroupMemberPO.setMemberName(memberResumePO.getName());
                        leadingGroupMemberPO.setOrgId(orgManagmentVO.getOrganizationPO().getOrgId());
                        leadingGroupMemberMapper.insert(leadingGroupMemberPO);
                        // 任命文件表业务字段
                        List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
                        bundledFile(appointDocuPOs, leadingMemberId);
                        break;
                    }
                    case UPDATE: {
                        //更新人员任命文件
                        String groupMemberId = memberResumePO.getLeadingMemberId();
                        List<PiisDocumentPO> documents = getDocumentsByObjectId(groupMemberId);
                        unbundledFile(documents);
                        List<PiisDocumentPO> appointDocuPOs1 = memberResumePO.getAppointDocuPOs();
                        bundledFile(appointDocuPOs1, groupMemberId);
                        //更新人员履历
                        BizUtils.setUpdatedOperation(MemberResumePO.class, memberResumePO);
                        BizUtils.setUpdatedTimeOperation(MemberResumePO.class, memberResumePO);
                        memberResumeMapper.updateById(memberResumePO);
                        //更新领导小组成员表
                        LeadingGroupMemberPO leadingGroupMemberPO = new LeadingGroupMemberPO();
                        leadingGroupMemberPO.setLeadingGroupId(leadingGroupId);
                        leadingGroupMemberPO.setMemberRole(memberResumePO.getRole());
                        leadingGroupMemberPO.setMemberPost(memberResumePO.getPost());
                        leadingGroupMemberPO.setMemberName(memberResumePO.getName());
                        leadingGroupMemberPO.setOrgId(orgManagmentVO.getOrganizationPO().getOrgId());
                        leadingGroupMemberMapper.updateById(leadingGroupMemberPO);
                        break;

                    }
                    case DELETE: {
                        // 删除成员任命文件
                        String groupMemberId = memberResumePO.getLeadingMemberId();
                        List<PiisDocumentPO> documents = getDocumentsByObjectId(groupMemberId);
                        unbundledFile(documents);
                        String memberId = memberResumePO.getMemberId();
                        // 删除成员履历表
                        delPhotoAndMemberResume(memberId);
                        // 删除领导小组成员表记录
                        QueryWrapper<LeadingGroupMemberPO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("leading_member_id", groupMemberId);
                        leadingGroupMemberMapper.delete(queryWrapper);
                        break;
                    }
                }

            } else {
                log.warn("memberResumePO operationType is null! memberResumePO = {}", memberResumePO);
            }

        });
    }


    private void saveGroupAndMember(OrgManagmentVO orgManagmentVO,
                                    LeadingGroupFoundPO leadingGroupFoundPO,
                                    List<MemberResumePO> memberResumePOS) {
        String leadingGroupId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        leadingGroupFoundPO.setLeadingGroupId(leadingGroupId);
        leadingGroupFoundPO.setOrgId(orgManagmentVO.getOrganizationPO().getOrgId());
        leadingGroupFoundPO.setOrgName(orgManagmentVO.getOrganizationPO().getOrgName());
        SysDept dept = sysDeptMapper.selectDeptById(orgManagmentVO.getOrganizationPO().getOrgId());
        leadingGroupFoundPO.setFatherOrgId(dept.getParentId());
        BizUtils.setCreatedOperation(LeadingGroupFoundPO.class, leadingGroupFoundPO);
        BizUtils.setCreatedTimeOperation(LeadingGroupFoundPO.class, leadingGroupFoundPO);
        leadingGroupFoundMapper.insert(leadingGroupFoundPO);
        List<PiisDocumentPO> groupFoundDocList = leadingGroupFoundPO.getGroupFoundDocList();
        bundledFile(groupFoundDocList, leadingGroupId);
        // 新增巡察领导小组成员表
        if (!CollectionUtils.isEmpty(memberResumePOS)) {
            memberResumePOS.forEach(memberResumePO -> {
                LeadingGroupMemberPO groupMemberPO = new LeadingGroupMemberPO();
                String leadingMemberId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
                groupMemberPO.setLeadingMemberId(leadingMemberId);
                groupMemberPO.setOrgId(orgManagmentVO.getOrganizationPO().getOrgId());
                groupMemberPO.setMemberId(memberResumePO.getMemberId());
                groupMemberPO.setMemberName(memberResumePO.getName());
                groupMemberPO.setMemberPost(memberResumePO.getPost());
                groupMemberPO.setMemberRole(memberResumePO.getRole());
                List<PiisDocumentPO> appointDocuPOs = memberResumePO.getAppointDocuPOs();
                bundledFile(appointDocuPOs, leadingMemberId);
                groupMemberPO.setLeadingGroupId(leadingGroupFoundPO.getLeadingGroupId());
                BizUtils.setCreatedOperation(LeadingGroupMemberPO.class, groupMemberPO);
                BizUtils.setCreatedTimeOperation(LeadingGroupMemberPO.class, groupMemberPO);
                leadingGroupMemberMapper.insert(groupMemberPO);

                MemberResumePO memberResumePO1 = memberResumeMapper.selectById(memberResumePO.getMemberId());
                if (null == memberResumePO1) {
                    // 新增人员履历表
                    memberResumePO1 = new MemberResumePO();
                    BizUtils.setCreatedOperation(MemberResumePO.class, memberResumePO1);
                    memberResumeMapper.insert(memberResumePO);
                }

            });
        }

    }


    private void unbundledFile(List<PiisDocumentPO> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(piisDocumentPO -> {
                piisDocumentPO.setObjectId("");
                documentMapper.updateById(piisDocumentPO);
            });
        }
    }

    private void bundledFile(List<PiisDocumentPO> documents, String objectId) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(piisDocumentPO -> {
                piisDocumentPO.setObjectId(objectId);
                documentMapper.updateById(piisDocumentPO);
            });
        }
    }
}
