package tech.piis.modules.person.service;

import tech.piis.modules.person.domain.vo.InspectionInfoVO;

import java.util.List;

/**
 * ClassName : ILeadingGroupFoundService
 * Package : tech.piis.modules.person.service
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface ILeadingGroupFoundService {
    /**
     * 新增巡察组成立信息、新增巡察组成员信息
     * @param leadingGroupFoundPO
     * @return
     */
//    int save(LeadingGroupFoundPO leadingGroupFoundPO);

    /**
     * 子公司巡察机构、人员数量预览
     * @param
     * @return
     */
    List<InspectionInfoVO> queryLeadingInspectionInfo();
}
