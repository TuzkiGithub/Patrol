package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionSpotMaterialPO;
import java.util.List;

/**
 * 驻场材料 Service接口
 *
 * @author Tuzki
 * @date 2020-11-23
 */
public interface IInspectionSpotMaterialService {

    /**
     * 查询驻场材料 列表
     * @param inspectionSpotMaterial
     * @return
     * @throws BaseException
     */
    List<InspectionSpotMaterialPO> selectInspectionSpotMaterialList(InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException;

    /**
    * 新增驻场材料 
    * @param inspectionSpotMaterial
    * @return
    * @throws BaseException
    */
    int save(InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException;

    /**
     * 根据ID修改驻场材料 
     * @param inspectionSpotMaterial
     * @return
     * @throws BaseException
     */
    int update(InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException;

    /**
     * 根据ID批量删除驻场材料 
     * @param inspectionSpotMaterialIds 驻场材料 编号
     *
     * @return
     */
    int deleteByInspectionSpotMaterialIds(Long[] inspectionSpotMaterialIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
