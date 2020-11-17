package tech.piis.modules.managment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.managment.domain.LeadingGroupMemberPO;
import tech.piis.modules.managment.domain.MemberResumePO;
import tech.piis.modules.managment.mapper.LeadingGroupMemberMapper;
import tech.piis.modules.managment.mapper.MemberResumeMapper;
import tech.piis.modules.managment.service.IMemberResumeService;

import javax.validation.constraints.NotBlank;

/**
 * ClassName : MemberResumeServiceImpl
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class MemberResumeServiceImpl implements IMemberResumeService {

    @Autowired
    private MemberResumeMapper memberResumeMapper;
    @Autowired
    private LeadingGroupMemberMapper leadingGroupMemberMapper;

    /**
     * 新增人员履历
     * @param memberResumePO
     * @return
     */
    @Override
    public int save(MemberResumePO memberResumePO) {
        return memberResumeMapper.insert(memberResumePO);
    }
}
