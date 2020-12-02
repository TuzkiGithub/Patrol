package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionReportApprovalPO;
import java.util.List;

/**
 * 报请审批 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionReportApprovalService {

    /**
     * 查询报请审批 列表
     * @param inspectionReportApproval
     * @return
     * @throws BaseException
     */
    List<InspectionReportApprovalPO> selectInspectionReportApprovalList(InspectionReportApprovalPO inspectionReportApproval) throws BaseException;

    /**
    * 新增报请审批 
    * @param inspectionReportApproval
    * @return
    * @throws BaseException
    */
    int save(InspectionReportApprovalPO inspectionReportApproval) throws BaseException;

    /**
     * 根据ID修改报请审批 
     * @param inspectionReportApproval
     * @return
     * @throws BaseException
     */
    int update(InspectionReportApprovalPO inspectionReportApproval) throws BaseException;

    /**
     * 根据ID批量删除报请审批 
     * @param reportApprovalIds 报请审批 编号
     *
     * @return
     */
    int deleteByInspectionReportApprovalIds(Long[] reportApprovalIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
