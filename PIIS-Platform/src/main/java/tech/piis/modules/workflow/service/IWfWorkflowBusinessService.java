package tech.piis.modules.workflow.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowBusinessPO;

import java.util.List;

/**
 * 流程业务 Service接口
 *
 * @author Tuzki
 * @date 2020-11-16
 */
public interface IWfWorkflowBusinessService {

    /**
     * 查询流程业务 列表
     * @param wfWorkflowBusiness
     * @return
     * @throws BaseException
     */
    List<WfWorkflowBusinessPO> selectWfWorkflowBusinessList(WfWorkflowBusinessPO wfWorkflowBusiness) throws BaseException;

    /**
    * 新增流程业务 
    * @param wfWorkflowBusiness
    * @return
    * @throws BaseException
    */
    int save(WfWorkflowBusinessPO wfWorkflowBusiness) throws BaseException;

    /**
     * 根据ID修改流程业务 
     * @param wfWorkflowBusiness
     * @return
     * @throws BaseException
     */
    int update(WfWorkflowBusinessPO wfWorkflowBusiness) throws BaseException;

    /**
     * 根据ID批量删除流程业务 
     * @param workflowBusinessIds 流程业务 编号
     *
     * @return
     */
    int deleteByWfWorkflowBusinessIds(Long[] workflowBusinessIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
