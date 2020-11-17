package tech.piis.modules.workflow.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.workflow.domain.po.WfWorkflowLinkPO;

import java.util.List;

/**
 *  流程线Service接口
 *
 * @author Tuzki
 * @date 2020-11-16
 */
public interface IWfWorkflowLinkService {

    /**
     * 查询 流程线列表
     * @param wfWorkflowLink
     * @return
     * @throws BaseException
     */
    List<WfWorkflowLinkPO> selectWfWorkflowLinkList(WfWorkflowLinkPO wfWorkflowLink) throws BaseException;

    /**
    * 新增 流程线
    * @param wfWorkflowLink
    * @return
    * @throws BaseException
    */
    int save(WfWorkflowLinkPO wfWorkflowLink) throws BaseException;

    /**
     * 根据ID修改 流程线
     * @param wfWorkflowLink
     * @return
     * @throws BaseException
     */
    int update(WfWorkflowLinkPO wfWorkflowLink) throws BaseException;

    /**
     * 根据ID批量删除 流程线
     * @param workflowLinkIds  流程线编号
     *
     * @return
     */
    int deleteByWfWorkflowLinkIds(Long[] workflowLinkIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
