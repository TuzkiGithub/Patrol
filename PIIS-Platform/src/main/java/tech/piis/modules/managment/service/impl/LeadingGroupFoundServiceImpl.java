package tech.piis.modules.managment.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.managment.domain.vo.InspectionInfoVO;
import tech.piis.modules.managment.mapper.LeadingGroupMemberMapper;
import tech.piis.modules.managment.service.ILeadingGroupFoundService;

import java.util.List;

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
    private LeadingGroupMemberMapper leadingGroupMemberMapper;

    /**
     * 子公司巡察机构、人员数量预览
     *
     * @param
     * @return
     */
    @Override
    public List<InspectionInfoVO> queryLeadingInspectionInfo() throws BaseException {
        return leadingGroupMemberMapper.queryLeadingInspectionInfo();
    }
}
