package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import java.util.List;

/**
 * 线索移交 Service接口
 *
 * @author Tuzki
 * @date 2020-10-27
 */
public interface IInspectionClueTransferService {

    /**
     * 统计巡视方案下被巡视单位InspectionClueTransfer次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionClueTransferCount(String planId) throws BaseException;

    /**
     * 查询线索移交 列表
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    List<InspectionClueTransferPO> selectInspectionClueTransferList(InspectionClueTransferPO inspectionClueTransfer) throws BaseException;

    /**
    * 新增线索移交 
    * @param inspectionClueTransfer
    * @return
    * @throws BaseException
    */
    int save(InspectionClueTransferPO inspectionClueTransfer) throws BaseException;

    /**
     * 根据ID修改线索移交 
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    int update(InspectionClueTransferPO inspectionClueTransfer) throws BaseException;

    /**
     * 根据ID批量删除线索移交 
     * @param clueTransferIds 线索移交 编号
     *
     * @return
     */
    int deleteByInspectionClueTransferIds(String[] clueTransferIds);

    /**
     * 统计总数
     * @return
     */
    int count(InspectionClueTransferPO inspectionClueTransfer);

    /**
     * 审批线索移交
     * @param clueTransferList
     */
    void doApprovals(List<InspectionClueTransferPO> clueTransferList);
}
