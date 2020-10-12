package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.domain.vo.PlanCompanyCountVO;

import java.util.List;

/**
 * 巡视计划 Mapper接口
 *
 * @author Tuzki
 * @date 2020-09-14
 */
public interface InspectionPlanMapper extends BaseMapper<InspectionPlanPO> {

    /**
     * 查询巡视计划列表
     *
     * @param inspectionPlanPO
     * @return
     */
    List<InspectionPlanPO> selectPlanList(InspectionPlanPO inspectionPlanPO);

    /**
     * 统计各公司巡察次数
     * @return
     */
    List<PlanCompanyCountVO> selectPlanCompanyTotal();
}
