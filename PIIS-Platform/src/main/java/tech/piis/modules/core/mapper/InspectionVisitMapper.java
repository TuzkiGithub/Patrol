package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionVisitPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 来访Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionVisitMapper extends BaseMapper<InspectionVisitPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionVisit次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionVisitCount(String planId) throws BaseException;

    /**
     * 查询来访及文件
     * @param inspectionVisitPO
     * @return
     * @throws BaseException
     */
    InspectionVisitPO selectVisitWithFile(InspectionVisitPO inspectionVisitPO) throws BaseException;
}