package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionRectificationPO;
import tech.piis.modules.core.domain.vo.RectificationCountVO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 整改公开情况Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-11
 */
public interface InspectionRectificationMapper extends BaseMapper<InspectionRectificationPO> {
    /**
     * 统计巡视方案下被巡视单位整改公开情况次数
     * @param planId 巡视计划ID
     *
     */
    List<RectificationCountVO> selectInspectionRectificationCount(String planId) throws BaseException;

    /**
    * 根据ID查询整改公开情况以及文件信息
    * @return
    * @throws BaseException
    */
    InspectionRectificationPO selectInspectionRectificationWithFileById(InspectionRectificationPO inspectionRectification) throws BaseException;
}
