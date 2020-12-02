package tech.piis.modules.core.service;


import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.domain.vo.PlanBriefVO;
import tech.piis.modules.core.domain.vo.PlanCompanyCountVO;
import tech.piis.modules.core.domain.vo.PlanConditionVO;
import tech.piis.modules.core.domain.vo.PlanMemberCountVO;

import java.util.List;

/**
 * 巡视计划 Service接口
 *
 * @author Tuzki
 * @date 2020-09-14
 */
public interface IInspectionPlanService {
    /**
     * 查询巡视计划列表
     *
     * @param inspectionPlanPO
     * @return
     */
    List<InspectionPlanPO> selectPlanList(InspectionPlanPO inspectionPlanPO);

    /**
     * 新增巡视计划
     *
     * @param inspectionPlanPO
     * @return
     */
    int savePlan(InspectionPlanPO inspectionPlanPO) throws Exception;


    /**
     * 修改巡视计划
     *
     * @param inspectionPlanPO
     * @return
     */
    int editPlan(InspectionPlanPO inspectionPlanPO) throws Exception;

    /**
     * 根据ID删除巡视计划
     *
     * @param planIds
     * @return
     */
    int delPlanByIds(String[] planIds);

    /**
     * 查询总记录数
     *
     * @return
     */
    int selectCount(InspectionPlanPO plan);

    /**
     * 统计公司巡察次数
     *
     * @return
     */
    List<PlanCompanyCountVO> selectCountByCompany(InspectionPlanPO plan);

    /**
     * 统计公司巡视成员数量
     *
     * @return
     */
    List<PlanCompanyCountVO> selectMemberCountByTime(PlanMemberCountVO planMemberCountVO);

    /**
     * 根据用户ID查询历史参与巡视情况
     *
     * @param userId
     * @return
     */
    List<PlanConditionVO> selectPiisConditionByUserId(String userId);

    /**
     * 统计巡视巡察项目、被巡视单位、巡视组数量
     *
     * @return
     */
    List selectPiisProjectCount();

    /**
     * 查询巡视简要信息
     *
     * @param planBriefDTO
     * @return
     * @throws BaseException
     */
    PlanBriefVO selectPiisBrief(PlanBriefDTO planBriefDTO) throws BaseException;

}
