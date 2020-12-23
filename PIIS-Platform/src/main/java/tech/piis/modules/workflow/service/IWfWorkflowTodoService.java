package tech.piis.modules.workflow.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.domain.vo.TodoCountVO;

import java.util.List;

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
    int saveWorkflowTodo(WfWorkFlowTodoPO wfWorkFlowTodoPO) throws BaseException;

    /**
     * 修改代办
     *
     * @param wfWorkFlowTodoPO
     * @return
     */
    int updateWorkflowTodoById(WfWorkFlowTodoPO wfWorkFlowTodoPO) throws BaseException;


    /**
     * 查询代办列表
     *
     * @param wfWorkflowTodo
     * @return
     * @throws BaseException
     */
    List<WfWorkFlowTodoPO> selectWfWorkflowTodoList(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException;


    /**
     * 查询代办详情
     *
     * @param workflowTodoId
     * @return
     * @throws BaseException
     */
    WfWorkFlowTodoPO selectWfWorkflowTodoById(Long workflowTodoId) throws BaseException;


    /**
     * 审批
     *
     * @param wfWorkflowTodo
     * @return
     * @throws BaseException
     */
    int doApproval(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException;

    /**
     * 查询代办已办数量
     *
     * @return
     */
    TodoCountVO todoCount(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException;
}
