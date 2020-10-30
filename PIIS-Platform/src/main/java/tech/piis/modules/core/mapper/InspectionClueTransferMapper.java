package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import java.util.List;
/**
 * 线索移交 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-10-27
 */
public interface InspectionClueTransferMapper extends BaseMapper<InspectionClueTransferPO> {

    /**
     * 统计巡视方案下被巡视单位InspectionClueTransfer次数
     * @param planId 巡视计划ID
     *
     */
    List<UnitsBizCountVO> selectInspectionClueTransferCount(String planId) throws BaseException;

    /**
     * 查询线索移交列表
     * @param inspectionClueTransferPO
     * @return
     * @throws BaseException
     */
    List<InspectionClueTransferPO> selectInspectionClueTransferList(InspectionClueTransferPO inspectionClueTransferPO) throws BaseException;
}
