package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.person.domain.po.DailyTrainingClassPO;

import java.util.List;


/**
 * ClassName : DailyTrainingClassMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface DailyTrainingClassMapper extends BaseMapper<DailyTrainingClassPO> {
    int insertBatch(List<DailyTrainingClassPO> classPOS);
}
