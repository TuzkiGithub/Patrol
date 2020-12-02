package tech.piis.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SpringBeanUtils;
import tech.piis.modules.core.event.WorkFlowEvent;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.domain.vo.TodoCountVO;
import tech.piis.modules.workflow.mapper.WfWorkflowTodoMapper;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.*;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.service.impl
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 15:16
 * Description:代办Service
 */
@Service
public class WorkflowTodoServiceImpl implements IWfWorkflowTodoService {

    @Autowired
    private WfWorkflowTodoMapper wfWorkflowTodoMapper;

    @Autowired
    private SpringBeanUtils springBeanUtils;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查询代办列表
     *
     * @param wfWorkflowTodo
     * @return
     * @throws BaseException
     */
    @Override
    public List<WfWorkFlowTodoPO> selectWfWorkflowTodoList(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException {
        QueryWrapper<WfWorkFlowTodoPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("APPROVER_ID", wfWorkflowTodo.getApproverId());
        if (null != wfWorkflowTodo.getTodoStatus()) {
            queryWrapper.eq("TODO_STATUS", wfWorkflowTodo.getTodoStatus());
        }
        queryWrapper.orderByDesc("CREATED_TIME");
        return wfWorkflowTodoMapper.selectList(queryWrapper);
    }

    /**
     * 查询代办详情
     *
     * @param workflowTodoId
     * @return
     * @throws BaseException
     */
    @Override
    public WfWorkFlowTodoPO selectWfWorkflowTodoById(Long workflowTodoId) throws BaseException {
        WfWorkFlowTodoPO wfWorkFlowTodoPO = wfWorkflowTodoMapper.selectById(workflowTodoId);
        Object object = springBeanUtils.getBeanByCustom(wfWorkFlowTodoPO.getTodoName());
        WorkFlowEvent event = new WorkFlowEvent(object);
        event.setEventType(OperationEnum.SELECT.getCode());
        event.setBizId(wfWorkFlowTodoPO.getBusinessId());
        event.setContinueApprovalFlag(wfWorkFlowTodoPO.getContinueApprovalFlag());
        event.setAgreeFlag(wfWorkFlowTodoPO.getTodoStatus());
        applicationContext.publishEvent(event);
        wfWorkFlowTodoPO.setBizObject(event.getData());
        return wfWorkFlowTodoPO;
    }

    /**
     * 审批
     *
     * @param wfWorkflowTodo
     * @return
     * @throws BaseException
     */
    @Override
    public int doApproval(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException {
        Integer continueApprovalFlag = wfWorkflowTodo.getContinueApprovalFlag();
        int result;
        //无需下一节点审批，更新代办状态，业务表状态
        if (NO_APPROVAL == continueApprovalFlag) {
            wfWorkflowTodo.setTodoStatus(TODO_FINISH);
            result = wfWorkflowTodoMapper.updateById(wfWorkflowTodo);
            Object object = springBeanUtils.getBeanByCustom(wfWorkflowTodo.getTodoName());
            WorkFlowEvent event = new WorkFlowEvent(object);
            event.setEventType(OperationEnum.UPDATE.getCode());
            event.setContinueApprovalFlag(wfWorkflowTodo.getContinueApprovalFlag());
            event.setBizId(wfWorkflowTodo.getBusinessId());
            event.setAgreeFlag(wfWorkflowTodo.getAgreeFlag());
            applicationContext.publishEvent(event);
        } else {
            WfWorkFlowTodoPO todoPO = new WfWorkFlowTodoPO()
                    .setApproverId(wfWorkflowTodo.getApproverId())
                    .setApproverName(wfWorkflowTodo.getApproverName())
                    .setTodoName(wfWorkflowTodo.getTodoName())
                    .setBusinessId(wfWorkflowTodo.getBusinessId())
                    .setLookStatus(NO_LOOK);
            BizUtils.setCreatedOperation(WfWorkFlowTodoPO.class, todoPO);
            result = wfWorkflowTodoMapper.insert(todoPO);
        }
        return result;
    }

    /**
     * 查询数量
     *
     * @param wfWorkflowTodo
     * @return
     */
    @Override
    public TodoCountVO todoCount(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException {
        return wfWorkflowTodoMapper.selectCount(wfWorkflowTodo);
    }

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
     * 根据ID修改代办
     *
     * @param wfWorkFlowTodoPO
     * @return
     */
    @Override
    public int updateWorkflowTodoById(WfWorkFlowTodoPO wfWorkFlowTodoPO) {
        return wfWorkflowTodoMapper.updateById(wfWorkFlowTodoPO);
    }

}
