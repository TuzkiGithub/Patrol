package tech.piis.modules.workflow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.mapper.WfWorkflowTodoMapper;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.service.impl
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 15:16
 * Description:代办Service
 */
@Service
public class WorkflowTodoServiceimpl implements IWfWorkflowTodoService {

    @Autowired
    private WfWorkflowTodoMapper wfWorkflowTodoMapper;

    /**
     * 新增代办
     *
     * @param wfWorkFlowTodoPO
     * @return
     */
    @Override
    public int saveWorkflowTodo(WfWorkFlowTodoPO wfWorkFlowTodoPO) {
        return wfWorkflowTodoMapper.insert(wfWorkFlowTodoPO);
    }

    /**
     * 修改代办
     *
     * @param wfWorkFlowTodoPO
     * @return
     */
    @Override
    public int updateWorkflowTodoById(WfWorkFlowTodoPO wfWorkFlowTodoPO) {
        return wfWorkflowTodoMapper.updateById(wfWorkFlowTodoPO);
    }
}
