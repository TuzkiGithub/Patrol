package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionProblemDraftPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;

import java.util.List;

/**
 * 问题底稿 Mapper接口
 *
 * @author Kevin
 * @date 2020-10-27
 */
public interface InspectionProblemDraftMapper extends BaseMapper<InspectionProblemDraftPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionProblemDraft次数
     *
     * @param planId 巡视计划ID
     */
    List<UnitsBizCountVO> selectInspectionProblemDraftCount(String planId) throws BaseException;

    /**
     * 查询问题底稿文件
     *
     * @return
     * @throws BaseException
     */
    InspectionProblemDraftPO selectInspectionProblemDraftWithFile(InspectionProblemDraftPO inspectionProblemDraftPO) throws BaseException;
}