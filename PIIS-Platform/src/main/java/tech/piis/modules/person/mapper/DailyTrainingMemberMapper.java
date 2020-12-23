package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.person.domain.po.DailyTrainingMemberPO;

import java.util.List;

/**
 * ClassName : DailyTrainingMemberMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface DailyTrainingMemberMapper extends BaseMapper<DailyTrainingMemberPO> {

    int insertBatch(List<DailyTrainingMemberPO> memberPOS);
}
