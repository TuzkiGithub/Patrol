package tech.piis.modules.person.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.person.domain.po.DailyTrainingPO;
import tech.piis.modules.person.domain.vo.DailyTrainingVO;

import java.util.List;

/**
 * ClassName : DailyTrainingMapper
 * Package : tech.piis.modules.person.mapper
 * Description :
 *
 * @author : chenhui@xvco.com
 */
public interface DailyTrainingMapper extends BaseMapper<DailyTrainingPO> {

    List<DailyTrainingPO> selectDailyTrainingByOrgId();

    /**
     * 日常培训信息
     * @param memberId
     * @return
     */
    List<DailyTrainingPO> selectBasicInfo(String memberId);

    /**
     * 查询列表及统计各个日常培训参与人数
     * @param dailyTrainingPO
     * @return
     */
    List<DailyTrainingPO> selectDailyTrainingListByConditions(DailyTrainingPO dailyTrainingPO);

    /**
     * 查询整合专业培训信息
     * @param memberId
     * @return
     */
    List<DailyTrainingVO> selectDailyTrainingInfo(String memberId);
}
