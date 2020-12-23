package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionTempBranchPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 临时支部 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-11-23
 */
public interface InspectionTempBranchMapper extends BaseMapper<InspectionTempBranchPO> {

    /**
     * 查询临时党支部
     * @param inspectionTempBranchPO
     * @return
     * @throws BaseException
     */
    InspectionTempBranchPO selectInspectionTempBranch(InspectionTempBranchPO inspectionTempBranchPO) throws BaseException;
}
