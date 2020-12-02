package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionIssuanceNoticePO;
import java.util.List;

/**
 * 印发通知 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionIssuanceNoticeService {

    /**
     * 查询印发通知 列表
     * @param inspectionIssuanceNotice
     * @return
     * @throws BaseException
     */
    List<InspectionIssuanceNoticePO> selectInspectionIssuanceNoticeList(InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException;

    /**
    * 新增印发通知 
    * @param inspectionIssuanceNotice
    * @return
    * @throws BaseException
    */
    int save(InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException;

    /**
     * 根据ID修改印发通知 
     * @param inspectionIssuanceNotice
     * @return
     * @throws BaseException
     */
    int update(InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException;

    /**
     * 根据ID批量删除印发通知 
     * @param issuanceNoticeIds 印发通知 编号
     *
     * @return
     */
    int deleteByInspectionIssuanceNoticeIds(Long[] issuanceNoticeIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
