package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionTempBranchPO;
import java.util.List;

/**
 * 临时支部 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionTempBranchService {

    /**
     * 查询临时支部 列表
     * @param inspectionTempBranch
     * @return
     * @throws BaseException
     */
    List<InspectionTempBranchPO> selectInspectionTempBranchList(InspectionTempBranchPO inspectionTempBranch) throws BaseException;

    /**
    * 新增临时支部 
    * @param inspectionTempBranch
    * @return
    * @throws BaseException
    */
    int save(InspectionTempBranchPO inspectionTempBranch) throws BaseException;

    /**
     * 根据ID修改临时支部 
     * @param inspectionTempBranch
     * @return
     * @throws BaseException
     */
    int update(InspectionTempBranchPO inspectionTempBranch) throws BaseException;

    /**
     * 根据ID批量删除临时支部 
     * @param tempBranchIds 临时支部 编号
     *
     * @return
     */
    int deleteByInspectionTempBranchIds(Long[] tempBranchIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
