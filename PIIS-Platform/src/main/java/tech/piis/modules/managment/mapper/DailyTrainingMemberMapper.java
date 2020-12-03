package tech.piis.modules.managment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.managment.domain.po.DailyTrainingMemberPO;

import java.util.List;

/**
 * ClassName : DailyTrainingMemberMapper
 * Package : tech.piis.modules.managment.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface DailyTrainingMemberMapper extends BaseMapper<DailyTrainingMemberPO> {

    int insertBatch(List<DailyTrainingMemberPO> memberPOS);
}
