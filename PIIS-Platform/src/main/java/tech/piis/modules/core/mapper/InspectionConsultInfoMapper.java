package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionConsultInfoPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 查阅资料Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionConsultInfoMapper extends BaseMapper<InspectionConsultInfoPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionConsultInfo次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionConsultInfoCount(String planId) throws BaseException;

    /**
     * 查询查阅资料
     * @param inspectionConsultInfo
     * @return
     */
    List<InspectionConsultInfoPO> selectInspectionConsultInfoList(InspectionConsultInfoPO inspectionConsultInfo);

    /**
     * 查询查阅资料
     * @param consultId
     * @return
     * @throws BaseException
     */
    InspectionConsultInfoPO selectInspectionConsultInfoById(String consultId) throws BaseException;
}
