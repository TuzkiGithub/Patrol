package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.core.domain.po.InspectionTalkOutlinePO;

import java.util.List;

/**
 * 谈话提纲Mapper接口
 *
 * @author Tuzki
 * @date 2020-11-04
 */
public interface InspectionTalkOutlineMapper extends BaseMapper<InspectionTalkOutlinePO> {

    /**
     * 查询谈话提纲
     *
     * @param planId
     * @return
     */
    List<InspectionTalkOutlinePO> selectTalkOutlineList(String planId);
}
