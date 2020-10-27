package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionIndividualTalkPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 个别谈话 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-10-27
 */
public interface InspectionIndividualTalkMapper extends BaseMapper<InspectionIndividualTalkPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionIndividualTalk次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionIndividualTalkCount(String planId) throws BaseException;
}
