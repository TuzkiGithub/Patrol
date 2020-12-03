package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionTempBranchMemberPO;
import java.util.List;

/**
 * 临时支部成员 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionTempBranchMemberService {

    /**
     * 查询临时支部成员 列表
     * @param inspectionTempBranchMember
     * @return
     * @throws BaseException
     */
    List<InspectionTempBranchMemberPO> selectInspectionTempBranchMemberList(InspectionTempBranchMemberPO inspectionTempBranchMember) throws BaseException;

    /**
    * 新增临时支部成员 
    * @param inspectionTempBranchMember
    * @return
    * @throws BaseException
    */
    int save(InspectionTempBranchMemberPO inspectionTempBranchMember) throws BaseException;

    /**
     * 根据ID修改临时支部成员 
     * @param inspectionTempBranchMember
     * @return
     * @throws BaseException
     */
    int update(InspectionTempBranchMemberPO inspectionTempBranchMember) throws BaseException;

    /**
     * 根据ID批量删除临时支部成员 
     * @param tempBranchMemberIds 临时支部成员 编号
     *
     * @return
     */
    int deleteByInspectionTempBranchMemberIds(Long[] tempBranchMemberIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
