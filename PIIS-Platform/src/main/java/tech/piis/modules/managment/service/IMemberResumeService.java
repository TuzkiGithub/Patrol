package tech.piis.modules.managment.service;

import tech.piis.modules.managment.domain.po.MemberResumePO;

/**
 * ClassName : IMemberResume
 * Package : tech.piis.modules.managment.service
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
