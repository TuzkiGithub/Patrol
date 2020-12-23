package tech.piis.modules.person.service;

import tech.piis.modules.person.domain.po.MemberResumePO;

/**
 * ClassName : IMemberResume
 * Package : tech.piis.modules.person.service
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface IMemberResumeService {
    /**
     * 新增人员履历
     * @param memberResumePO
     * @return
     */
    int save(MemberResumePO memberResumePO);
}
