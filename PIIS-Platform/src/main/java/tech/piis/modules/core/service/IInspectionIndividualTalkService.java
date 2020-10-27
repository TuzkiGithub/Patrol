package tech.piis.modules.core.service;

import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.domain.po.InspectionIndividualTalkPO;
import java.util.List;

/**
 * 个别谈话 Service接口
 *
 * @author Tuzki
 * @date 2020-10-27
 */
public interface IInspectionIndividualTalkService {

    /**
     * 统计巡视方案下被巡视单位InspectionIndividualTalk次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionIndividualTalkCount(String planId) throws BaseException;

    /**
     * 查询个别谈话 列表
     * @param inspectionIndividualTalk
     * @return
     * @throws BaseException
     */
    List<InspectionIndividualTalkPO> selectInspectionIndividualTalkList(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException;

    /**
    * 新增个别谈话 
    * @param inspectionIndividualTalk
    * @return
    * @throws BaseException
    */
    int save(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException;

    /**
     * 根据ID修改个别谈话 
     * @param inspectionIndividualTalk
     * @return
     * @throws BaseException
     */
    int update(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException;

    /**
     * 根据ID批量删除个别谈话 
     * @param individualTalkIds 个别谈话 编号
     *
     * @return
     */
    int deleteByInspectionIndividualTalkIds(Long[] individualTalkIds);

    /**
     * 统计总数
     * @return
     */
    int count();
}
