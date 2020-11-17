package tech.piis.modules.survey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.survey.domain.po.SurveyOptionPO;

import java.util.List;

/**
 * 选项Mapper接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface SurveyOptionMapper extends BaseMapper<SurveyOptionPO> {

    /**
     * 批量新增题目
     * @param optionList
     * @return
     * @throws BaseException
     */
    int insertOptionBatch(List<SurveyOptionPO> optionList) throws BaseException;
}
