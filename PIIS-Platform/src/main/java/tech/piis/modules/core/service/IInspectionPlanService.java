package tech.piis.modules.core.service;


import tech.piis.modules.core.domain.po.InspectionPlanPO;

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
     * @param inspectionPlanPO
     * @return
     */
    int savePlan(InspectionPlanPO inspectionPlanPO);


    /**
     * 修改巡视计划
     * @param inspectionPlanPO
     * @return
     */
    int editPlan(InspectionPlanPO inspectionPlanPO);

    /**
     * 根据ID删除巡视计划
     * @param planIds
     * @return
     */
    int delPlanByIds(String[] planIds);
}
