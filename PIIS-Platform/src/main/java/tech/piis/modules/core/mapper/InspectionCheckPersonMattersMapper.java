package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionCheckPersonMattersPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 抽查个人事项报告Mapper接口
 * 
 * @author Kevin
 * @date 2020-10-19
 */
public interface InspectionCheckPersonMattersMapper extends BaseMapper<InspectionCheckPersonMattersPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionCheckPersonMatters次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionCheckPersonMattersCount(String planId) throws BaseException;

    /**
     * 查询抽查报告以及文件
     * @param checkPersonMattersPO
     * @return
     * @throws BaseException
     */
    List<InspectionCheckPersonMattersPO> selectInspectionCheckPersonMattersList(InspectionCheckPersonMattersPO checkPersonMattersPO) throws BaseException;
}
