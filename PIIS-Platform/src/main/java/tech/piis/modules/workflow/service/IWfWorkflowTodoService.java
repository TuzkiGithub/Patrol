package tech.piis.modules.workflow.service;

import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.service
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 15:15
 * Description:代办Service
 */
public interface IWfWorkflowTodoService {

    /**
     * 新增代办
     *
     * @param wfWorkFlowTodoPO
     * @return
     */
    int saveWorkflowTodo(WfWorkFlowTodoPO wfWorkFlowTodoPO);

    /**
     * 修改代办
     *
     * @param wfWorkFlowTodoPO
     * @return
     */
    int updateWorkflowTodoById(WfWorkFlowTodoPO wfWorkFlowTodoPO);
}
