package tech.piis.modules.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.domain.vo.TodoCountVO;
import tech.piis.modules.workflow.service.IWfWorkflowTodoService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.LOOK_FINISH;
import static tech.piis.common.constant.PiisConstants.TODO_NEED;


/**
 * 代办Controller
 *
 * @author Tuzki
 * @date 2020-11-26
 */
@RestController
@RequestMapping("/piis/todo")
public class WfWorkflowTodoController extends BaseController {
    @Autowired
    private IWfWorkflowTodoService wfWorkflowTodoService;



    /**
     * 查询代办列表
     *
     * @param wfWorkflowTodo
     */
    @GetMapping("/list")
    public AjaxResult list(WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException {
        startPage();
        List<WfWorkFlowTodoPO> data = wfWorkflowTodoService.selectWfWorkflowTodoList(wfWorkflowTodo);
        AjaxResult ajaxResult = AjaxResult.success();
        TableDataInfo tableDataInfo = getDataTable(data);
        ajaxResult.put("rows", tableDataInfo.getRows());
        ajaxResult.put("code", tableDataInfo.getCode());
        ajaxResult.put("msg", tableDataInfo.getMsg());
        TodoCountVO todoCountVO = wfWorkflowTodoService.todoCount(new WfWorkFlowTodoPO().setApproverId(wfWorkflowTodo.getApproverId()).setTodoStatus(TODO_NEED));
        if (null != todoCountVO) {
            ajaxResult.put("undoCount", todoCountVO.getUndoCount());
            ajaxResult.put("doCount", todoCountVO.getDoCount());
            ajaxResult.put("total", todoCountVO.getUndoCount() +todoCountVO.getDoCount());

        }
        return ajaxResult;
    }

    /**
     * 查询代办详情
     *
     * @param workflowTodoId
     */
    @PreAuthorize("@ss.hasPermi('workflow:todo:perms')")
    @GetMapping("/info")
    public AjaxResult todoDetail(Long workflowTodoId) {
        return AjaxResult.success(wfWorkflowTodoService.selectWfWorkflowTodoById(workflowTodoId));
    }

    /**
     * 修改代办状态
     *
     * @param wfWorkflowTodo
     */
    @PreAuthorize("@ss.hasPermi('workflow:todo:perms')")
    @Log(title = "代办", businessType = BusinessType.UPDATE)
    @PostMapping("/status")
    public AjaxResult edit(@RequestBody WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException {
        if (null == wfWorkflowTodo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        wfWorkflowTodo.setLookStatus(LOOK_FINISH);
        BizUtils.setUpdatedOperation(WfWorkFlowTodoPO.class, wfWorkflowTodo);
        return toAjax(wfWorkflowTodoService.updateWorkflowTodoById(wfWorkflowTodo));
    }

    /**
     * 审批
     *
     * @param wfWorkflowTodo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('workflow:todo:perms')")
    @PostMapping("approval")
    public AjaxResult doApproval(@RequestBody @Valid WfWorkFlowTodoPO wfWorkflowTodo) throws BaseException {
        wfWorkflowTodoService.doApproval(wfWorkflowTodo);
        return AjaxResult.success();
    }

}