package tech.piis.modules.workflow.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowNodePO;

import java.util.List;

/**
 * 流程节点 Service接口
 *
 * @author Tuzki
 * @date 2020-11-16
 */
public interface IWfWorkflowNodeService {

    /**
     * 查询流程节点 列表
     * @param wfWorkflowNode
     * @return
     * @throws BaseException
     */
    List<WfWorkflowNodePO> selectWfWorkflowNodeList(WfWorkflowNodePO wfWorkflowNode) throws BaseException;

    /**
    * 新增流程节点 
    * @param wfWorkflowNode
    * @return
    * @throws BaseException
    */
    int save(WfWorkflowNodePO wfWorkflowNode) throws BaseException;

    /**
     * 根据ID修改流程节点 
     * @param wfWorkflowNode
     * @return
     * @throws BaseException
     */
    int update(WfWorkflowNodePO wfWorkflowNode) throws BaseException;

    /**
     * 根据ID批量删除流程节点 
     * @param workflowNodeIds 流程节点 编号
     *
     * @return
     */
    int deleteByWfWorkflowNodeIds(Long[] workflowNodeIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
