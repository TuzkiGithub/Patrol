package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionPatrolReportPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 巡视报告Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-11
 */
public interface InspectionPatrolReportMapper extends BaseMapper<InspectionPatrolReportPO> {
    /**
     * 统计巡视方案下被巡视单位巡视报告次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionPatrolReportCount(String planId) throws BaseException;

    /**
    * 根据ID查询巡视报告以及文件信息
    * @return
    * @throws BaseException
    */
    InspectionPatrolReportPO selectInspectionPatrolReportWithFileById(InspectionPatrolReportPO inspectionPatrolReport) throws BaseException;
}
