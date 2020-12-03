package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionCallVisitPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

import java.util.List;

/**
 * 来电Mapper接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionCallVisitMapper extends BaseMapper<InspectionCallVisitPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionCallVisit次数
     *
     * @param planId 巡视计划ID
     */
    List<UnitsBizCountVO> selectInspectionCallVisitCount(String planId) throws BaseException;


    /**
     * 查询来访及文件
     *
     * @param callVisitPO
     * @return
     * @throws BaseException
     */
    InspectionCallVisitPO selectVisitWithFile(InspectionCallVisitPO callVisitPO) throws BaseException;
}
