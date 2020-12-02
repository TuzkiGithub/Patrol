package tech.piis.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkFlowTodoPO;
import tech.piis.modules.workflow.domain.vo.TodoCountVO;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.mapper
 * User: Tuzki
 * Date: 2020/11/16
 * Time: 15:13
 * Description:代办mapper
 */
public interface WfWorkflowTodoMapper extends BaseMapper<WfWorkFlowTodoPO> {

    /**
     * 查询代办，已办数量
     * @param wfWorkFlowTodoPO
     * @return
     * @throws BaseException
     */
    TodoCountVO selectCount(WfWorkFlowTodoPO wfWorkFlowTodoPO) throws BaseException;
}
