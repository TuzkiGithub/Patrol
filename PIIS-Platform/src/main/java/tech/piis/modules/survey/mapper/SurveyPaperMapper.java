package tech.piis.modules.survey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.po.SurveyPaperPO;

import java.util.List;

/**
 * 试卷/问卷Mapper接口
 *
 * @author Tuzki
 * @date 2020-11-09
 */
public interface SurveyPaperMapper extends BaseMapper<SurveyPaperPO> {

    List<SurveyPaperPO> selectSurveyPaperList(SurveyPaperPO paper) throws BaseException;
}
