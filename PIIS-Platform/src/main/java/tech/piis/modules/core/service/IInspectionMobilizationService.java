package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionMobilizationPO;
import java.util.List;

/**
 * 巡视动员会Service接口
 *
 * @author Tuzki
 * @date 2020-11-25
 */
public interface IInspectionMobilizationService {

    /**
     * 查询巡视动员会列表
     * @param inspectionMobilization
     * @return
     * @throws BaseException
     */
    List<InspectionMobilizationPO> selectInspectionMobilizationList(InspectionMobilizationPO inspectionMobilization) throws BaseException;

    /**
    * 新增巡视动员会
    * @param inspectionMobilization
    * @return
    * @throws BaseException
    */
    int save(InspectionMobilizationPO inspectionMobilization) throws BaseException;

    /**
     * 根据ID修改巡视动员会
     * @param inspectionMobilization
     * @return
     * @throws BaseException
     */
    int update(InspectionMobilizationPO inspectionMobilization) throws BaseException;

    /**
     * 根据ID批量删除巡视动员会
     * @param mobilizationIds 巡视动员会编号
     *
     * @return
     */
    int deleteByInspectionMobilizationIds(Long[] mobilizationIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}