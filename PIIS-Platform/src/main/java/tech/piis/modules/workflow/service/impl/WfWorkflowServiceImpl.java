package tech.piis.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowPO;
import tech.piis.modules.workflow.mapper.WfWorkflowMapper;
import tech.piis.modules.workflow.service.IWfWorkflowService;

import java.util.Arrays;
import java.util.List;


/**
 * 流程 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-16
 */
@Transactional
@Service
public class WfWorkflowServiceImpl implements IWfWorkflowService {
    @Autowired
    private WfWorkflowMapper wfWorkflowMapper;


    /**
     * 查询流程 列表
     *
     * @param wfWorkflow
     * @return
     * @throws BaseException
     */
    @Override
    public List<WfWorkflowPO> selectWfWorkflowList(WfWorkflowPO wfWorkflow) throws BaseException {
        QueryWrapper<WfWorkflowPO> queryWrapper = new QueryWrapper<>();
        return wfWorkflowMapper.selectList(queryWrapper);
    }

    /**
     * 新增流程
     *
     * @param wfWorkflow
     * @return
     * @throws BaseException
     */
    @Override
    public int save(WfWorkflowPO wfWorkflow) throws BaseException {
        int result = wfWorkflowMapper.insert(wfWorkflow);
        return result;
    }

    /**
     * 根据ID修改流程
     *
     * @param wfWorkflow
     * @return
     * @throws BaseException
     */
    @Override
    public int update(WfWorkflowPO wfWorkflow) throws BaseException {
        return wfWorkflowMapper.updateById(wfWorkflow);
    }

    /**
     * 根据ID批量删除流程
     *
     * @param workFlowIds 流程 编号
     * @return
     */
    @Override
    public int deleteByWfWorkflowIds(Long[] workFlowIds) {
        List<Long> list = Arrays.asList(workFlowIds);
        return wfWorkflowMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return wfWorkflowMapper.selectCount(null);
    }
}
