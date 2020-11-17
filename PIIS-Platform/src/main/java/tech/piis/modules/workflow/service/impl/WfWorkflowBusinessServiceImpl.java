package tech.piis.modules.workflow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowBusinessPO;
import tech.piis.modules.workflow.mapper.WfWorkflowBusinessMapper;
import tech.piis.modules.workflow.service.IWfWorkflowBusinessService;

import java.util.Arrays;
import java.util.List;


/**
 * 流程业务 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-16
 */
@Transactional
@Service
public class WfWorkflowBusinessServiceImpl implements IWfWorkflowBusinessService {
    @Autowired
    private WfWorkflowBusinessMapper wfWorkflowBusinessMapper;


    /**
     * 查询流程业务 列表
     *
     * @param wfWorkflowBusiness
     * @return
     * @throws BaseException
     */
    @Override
    public List<WfWorkflowBusinessPO> selectWfWorkflowBusinessList(WfWorkflowBusinessPO wfWorkflowBusiness) throws BaseException {
        return wfWorkflowBusinessMapper.selectList(null);
    }

    /**
     * 新增流程业务
     *
     * @param wfWorkflowBusiness
     * @return
     * @throws BaseException
     */
    @Override
    public int save(WfWorkflowBusinessPO wfWorkflowBusiness) throws BaseException {
        return wfWorkflowBusinessMapper.insert(wfWorkflowBusiness);
    }

    /**
     * 根据ID修改流程业务
     *
     * @param wfWorkflowBusiness
     * @return
     * @throws BaseException
     */
    @Override
    public int update(WfWorkflowBusinessPO wfWorkflowBusiness) throws BaseException {
        return wfWorkflowBusinessMapper.updateById(wfWorkflowBusiness);
    }

    /**
     * 根据ID批量删除流程业务
     *
     * @param workflowBusinessIds 流程业务 编号
     * @return
     */
    @Override
    public int deleteByWfWorkflowBusinessIds(Long[] workflowBusinessIds) {
        List<Long> list = Arrays.asList(workflowBusinessIds);
        return wfWorkflowBusinessMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return wfWorkflowBusinessMapper.selectCount(null);
    }
}
