package tech.piis.modules.workflow.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowPO;

import java.util.List;

/**
 * 流程 Service接口
 *
 * @author Tuzki
 * @date 2020-11-16
 */
public interface IWfWorkflowService {

    /**
     * 查询流程 列表
     * @param wfWorkflow
     * @return
     * @throws BaseException
     */
    List<WfWorkflowPO> selectWfWorkflowList(WfWorkflowPO wfWorkflow) throws BaseException;

    /**
    * 新增流程 
    * @param wfWorkflow
    * @return
    * @throws BaseException
    */
    int save(WfWorkflowPO wfWorkflow) throws BaseException;

    /**
     * 根据ID修改流程 
     * @param wfWorkflow
     * @return
     * @throws BaseException
     */
    int update(WfWorkflowPO wfWorkflow) throws BaseException;

    /**
     * 根据ID批量删除流程 
     * @param workFlowIds 流程 编号
     *
     * @return
     */
    int deleteByWfWorkflowIds(Long[] workFlowIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
