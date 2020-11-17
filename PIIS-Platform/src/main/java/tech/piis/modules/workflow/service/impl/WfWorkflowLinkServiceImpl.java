package tech.piis.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowLinkPO;
import tech.piis.modules.workflow.mapper.WfWorkflowLinkMapper;
import tech.piis.modules.workflow.service.IWfWorkflowLinkService;

import java.util.Arrays;
import java.util.List;


/**
 * 流程线Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-16
 */
@Transactional
@Service
public class WfWorkflowLinkServiceImpl implements IWfWorkflowLinkService {
    @Autowired
    private WfWorkflowLinkMapper wfWorkflowLinkMapper;


    /**
     * 查询 流程线列表
     *
     * @param wfWorkflowLink
     * @return
     * @throws BaseException
     */
    @Override
    public List<WfWorkflowLinkPO> selectWfWorkflowLinkList(WfWorkflowLinkPO wfWorkflowLink) throws BaseException {
        QueryWrapper<WfWorkflowLinkPO> queryWrapper = new QueryWrapper<>();
        return wfWorkflowLinkMapper.selectList(queryWrapper);
    }

    /**
     * 新增 流程线
     *
     * @param wfWorkflowLink
     * @return
     * @throws BaseException
     */
    @Override
    public int save(WfWorkflowLinkPO wfWorkflowLink) throws BaseException {
        int result = wfWorkflowLinkMapper.insert(wfWorkflowLink);
        return result;
    }

    /**
     * 根据ID修改 流程线
     *
     * @param wfWorkflowLink
     * @return
     * @throws BaseException
     */
    @Override
    public int update(WfWorkflowLinkPO wfWorkflowLink) throws BaseException {
        return wfWorkflowLinkMapper.updateById(wfWorkflowLink);
    }

    /**
     * 根据ID批量删除 流程线
     *
     * @param workflowLinkIds 流程线编号
     * @return
     */
    @Override
    public int deleteByWfWorkflowLinkIds(Long[] workflowLinkIds) {
        List<Long> list = Arrays.asList(workflowLinkIds);
        return wfWorkflowLinkMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return wfWorkflowLinkMapper.selectCount(null);
    }
}
