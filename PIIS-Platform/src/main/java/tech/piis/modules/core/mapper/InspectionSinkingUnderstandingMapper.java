package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

import java.util.List;

/**
 * 下沉了解Mapper接口
 *
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionSinkingUnderstandingMapper extends BaseMapper<InspectionSinkingUnderstandingPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstanding次数
     *
     * @param planId 巡视计划ID
     */
    List<UnitsBizCountVO> selectInspectionSinkingUnderstandingCount(String planId) throws BaseException;

    /**
     * 查询下沉了解
     * @param sinkingUnderstanding
     * @return
     * @throws BaseException
     */
    List<InspectionSinkingUnderstandingPO> selectInspectionSinkingUnderstandingList(InspectionSinkingUnderstandingPO sinkingUnderstanding) throws BaseException;

    /**
     * 查询下沉了解以及文件
     * @param sinkingUnderstanding
     * @return
     * @throws BaseException
     */
    InspectionSinkingUnderstandingPO selectSinkingUnderstandingWithFile(InspectionSinkingUnderstandingPO sinkingUnderstanding) throws BaseException;
}
