package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionClueTransferDetailPO;
import java.util.List;

/**
 * 线索移交详情 Service接口
 *
 * @author Tuzki
 * @date 2020-10-27
 */
public interface IInspectionClueTransferDetailService {

    /**
     * 统计巡视方案下被巡视单位InspectionClueTransferDetail次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionClueTransferDetailCount(String planId) throws BaseException;

    /**
     * 查询线索移交详情 列表
     * @param inspectionClueTransferDetail
     * @return
     * @throws BaseException
     */
    List<InspectionClueTransferDetailPO> selectInspectionClueTransferDetailList(InspectionClueTransferDetailPO inspectionClueTransferDetail) throws BaseException;

    /**
    * 新增线索移交详情 
    * @param inspectionClueTransferDetail
    * @return
    * @throws BaseException
    */
    int save(InspectionClueTransferDetailPO inspectionClueTransferDetail) throws BaseException;

    /**
     * 根据ID修改线索移交详情 
     * @param inspectionClueTransferDetail
     * @return
     * @throws BaseException
     */
    int update(InspectionClueTransferDetailPO inspectionClueTransferDetail) throws BaseException;

    /**
     * 根据ID批量删除线索移交详情 
     * @param clueTransferDetailIds 线索移交详情 编号
     *
     * @return
     */
    int deleteByInspectionClueTransferDetailIds(Long[] clueTransferDetailIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
