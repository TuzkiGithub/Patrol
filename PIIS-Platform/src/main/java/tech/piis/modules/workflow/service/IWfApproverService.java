package tech.piis.modules.workflow.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfApproverPO;

import java.util.List;

/**
 * 审批人员 Service接口
 *
 * @author Tuzki
 * @date 2020-11-16
 */
public interface IWfApproverService {

    /**
     * 查询审批人员 列表
     * @param wfApprover
     * @return
     * @throws BaseException
     */
    List<WfApproverPO> selectWfApproverList(WfApproverPO wfApprover) throws BaseException;

    /**
    * 新增审批人员 
    * @param wfApprover
    * @return
    * @throws BaseException
    */
    int save(WfApproverPO wfApprover) throws BaseException;

    /**
     * 根据ID修改审批人员 
     * @param wfApprover
     * @return
     * @throws BaseException
     */
    int update(WfApproverPO wfApprover) throws BaseException;

    /**
     * 根据ID批量删除审批人员 
     * @param approverIds 审批人员 编号
     *
     * @return
     */
    int deleteByWfApproverIds(Long[] approverIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
