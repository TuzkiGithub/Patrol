package tech.piis.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.workflow.domain.po.WfApproverPO;
import tech.piis.modules.workflow.mapper.WfApproverMapper;
import tech.piis.modules.workflow.service.IWfApproverService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;


/**
 * 审批人员 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-16
 */
@Transactional
@Service
public class WfApproverServiceImpl implements IWfApproverService {
    @Autowired
    private WfApproverMapper wfApproverMapper;


    /**
     * 查询审批人员 列表
     * @param wfApprover
     * @return
     * @throws BaseException
     */
    @Override
    public List<WfApproverPO> selectWfApproverList(WfApproverPO wfApprover) throws BaseException {
        QueryWrapper<WfApproverPO> queryWrapper = new QueryWrapper<>();
        return wfApproverMapper.selectList(queryWrapper);
    }

    /**
     * 新增审批人员 
     * @param wfApprover
     * @return
     * @throws BaseException
     */
    @Override
    public int save(WfApproverPO wfApprover) throws BaseException {
        int result = wfApproverMapper.insert(wfApprover);
        return result;
    }

    /**
     * 根据ID修改审批人员 
     * @param wfApprover
     * @return
     * @throws BaseException
     */
    @Override
    public int update(WfApproverPO wfApprover) throws BaseException {
        return wfApproverMapper.updateById(wfApprover);
    }

    /**
     * 根据ID批量删除审批人员 
     * @param approverIds 审批人员 编号
     *
     * @return
     */
    @Override
    public int deleteByWfApproverIds(Long[]approverIds) {
        List<Long> list = Arrays.asList(approverIds);
        return wfApproverMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return wfApproverMapper.selectCount(null);
    }
}
