package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionAnnouncementInfoPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 公告信息 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-12-03
 */
public interface InspectionAnnouncementInfoMapper extends BaseMapper<InspectionAnnouncementInfoPO> {
    /**
     * 统计巡视方案下被巡视单位公告信息次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionAnnouncementInfoCount(String planId) throws BaseException;

}
