package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionConsultInfoPO;
import java.util.List;

/**
 * 查阅资料Service接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface IInspectionConsultInfoService {

    /**
     * 统计巡视方案下被巡视单位InspectionConsultInfo次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionConsultInfoCount(String planId) throws BaseException;

    /**
     * 查询查阅资料列表
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    List<InspectionConsultInfoPO> selectInspectionConsultInfoList(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException;

    /**
    * 新增查阅资料
    * @param inspectionConsultInfo
    * @return
    * @throws BaseException
    */
    int save(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException;

    /**
     * 根据ID修改查阅资料
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    int update(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException;

    /**
     * 根据ID批量删除查阅资料
     * @param consultInfoIds 查阅资料编号
     *
     * @return
     */
    int deleteByInspectionConsultInfoIds(String[] consultInfoIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
