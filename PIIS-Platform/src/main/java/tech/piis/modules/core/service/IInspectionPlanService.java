package tech.piis.modules.core.service;


import org.springframework.web.multipart.MultipartFile;
import tech.piis.modules.core.domain.dto.InspectionPlanSaveDTO;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PlanCompanyCountVO;

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
    int savePlan(InspectionPlanPO inspectionPlanPO) throws Exception;


    /**
     * 修改巡视计划
     * @param inspectionPlanPO
     * @return
     */
    int editPlan(InspectionPlanPO inspectionPlanPO) throws Exception;

    /**
     * 根据ID删除巡视计划
     * @param planIds
     * @return
     */
    int delPlanByIds(String[] planIds);

    /**
     * 查询总记录数
     * @return
     */
    int selectCount(InspectionPlanPO plan);

    /**
     * 统计公司巡察次数
     * @return
     */
    List<PlanCompanyCountVO> selectCountByCompany(InspectionPlanPO plan);

}
