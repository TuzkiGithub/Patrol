package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.dto.PlanBriefDTO;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.domain.vo.*;

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
    List<InspectionPlanPO> selectPlanList(@Param("plan") InspectionPlanPO inspectionPlanPO);

    /**
     * 统计各公司巡察次数
     *
     * @return
     */
    List<PlanCompanyCountVO> selectPlanCompanyTotal(InspectionPlanPO plan);

    /**
     * 统计一级公司巡视成员数量
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
    List<PiisProjectCountVO> selectPiisProjectCount();

    /**
     * 查询巡视简要信息
     *
     * @param planBriefDTO
     * @return
     * @throws BaseException
     */
    PlanBriefVO selectPiisBrief(PlanBriefDTO planBriefDTO) throws BaseException;
}
