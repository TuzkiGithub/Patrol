package tech.piis.modules.managment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.managment.domain.LeadingGroupFoundPO;
import tech.piis.modules.managment.domain.LeadingGroupMemberPO;
import tech.piis.modules.managment.domain.vo.InspectionInfoVO;
import tech.piis.modules.managment.mapper.LeadingGroupFoundMapper;
import tech.piis.modules.managment.mapper.LeadingGroupMemberMapper;
import tech.piis.modules.managment.service.ILeadingGroupFoundService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * ClassName : LeadingGroupFoundServiceImpl
 * Package : tech.piis.modules.managment.service.impl
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Service
@Transactional
public class LeadingGroupFoundServiceImpl implements ILeadingGroupFoundService {
    @Autowired
    private LeadingGroupFoundMapper leadingGroupFoundMapper;
    @Autowired
    private LeadingGroupMemberMapper leadingGroupMemberMapper;


    @Override
    public int save(LeadingGroupFoundPO leadingGroupFoundPO) {
        // 新增巡察组成员信息
        List<LeadingGroupMemberPO> memberPOList = leadingGroupFoundPO.getMemberPOList();
        memberPOList.forEach(leadingGroupMemberPO -> {
            leadingGroupMemberMapper.insert(leadingGroupMemberPO);
        });
        // 新增巡察组成立信息
        return leadingGroupFoundMapper.insert(leadingGroupFoundPO);
    }

    /**
     * 子公司巡察机构、人员数量预览
     * @param
     * @return
     *
     */
    @Override
    public List<InspectionInfoVO> queryLeadingInspectionInfo() {
        return leadingGroupMemberMapper.queryLeadingInspectionInfo();
    }
}
