package tech.piis.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.workflow.domain.po.WfWorkflowNodePO;
import tech.piis.modules.workflow.mapper.WfWorkflowNodeMapper;
import tech.piis.modules.workflow.service.IWfWorkflowNodeService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;


/**
 * 流程节点 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-16
 */
@Transactional
@Service
public class WfWorkflowNodeServiceImpl implements IWfWorkflowNodeService {
    @Autowired
    private WfWorkflowNodeMapper wfWorkflowNodeMapper;


    /**
     * 查询流程节点 列表
     * @param wfWorkflowNode
     * @return
     * @throws BaseException
     */
    @Override
    public List<WfWorkflowNodePO> selectWfWorkflowNodeList(WfWorkflowNodePO wfWorkflowNode) throws BaseException {
        QueryWrapper<WfWorkflowNodePO> queryWrapper = new QueryWrapper<>();
        return wfWorkflowNodeMapper.selectList(queryWrapper);
    }

    /**
     * 新增流程节点 
     * @param wfWorkflowNode
     * @return
     * @throws BaseException
     */
    @Override
    public int save(WfWorkflowNodePO wfWorkflowNode) throws BaseException {
        int result = wfWorkflowNodeMapper.insert(wfWorkflowNode);
        return result;
    }

    /**
     * 根据ID修改流程节点 
     * @param wfWorkflowNode
     * @return
     * @throws BaseException
     */
    @Override
    public int update(WfWorkflowNodePO wfWorkflowNode) throws BaseException {
        return wfWorkflowNodeMapper.updateById(wfWorkflowNode);
    }

    /**
     * 根据ID批量删除流程节点 
     * @param workflowNodeIds 流程节点 编号
     *
     * @return
     */
    @Override
    public int deleteByWfWorkflowNodeIds(Long[]workflowNodeIds) {
        List<Long> list = Arrays.asList(workflowNodeIds);
        return wfWorkflowNodeMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return wfWorkflowNodeMapper.selectCount(null);
    }
}
