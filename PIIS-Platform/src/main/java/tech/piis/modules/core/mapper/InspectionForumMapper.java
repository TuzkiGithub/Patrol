package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionForumPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 座谈会 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-07
 */
public interface InspectionForumMapper extends BaseMapper<InspectionForumPO> {
    /**
     * 统计巡视方案下被巡视单位座谈会 次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionForumCount(String planId) throws BaseException;

    /**
    * 根据ID查询座谈会 以及文件信息
    * @return
    * @throws BaseException
    */
    InspectionForumPO selectInspectionForumWithFileById(InspectionForumPO inspectionForum) throws BaseException;
}
