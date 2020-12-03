package tech.piis.modules.survey.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.survey.domain.dto.PaperResultDTO;
import tech.piis.modules.survey.domain.po.SurveyResultPO;

import java.util.List;

/**
 * 试卷/问卷回答情况Mapper接口
 *
 * @author Tuzki
 * @date 2020-11-18
 */
public interface SurveyResultMapper extends BaseMapper<SurveyResultPO> {

    List<SurveyResultPO> selectPaperResultList(PaperResultDTO paperResultDTO) throws BaseException;
}