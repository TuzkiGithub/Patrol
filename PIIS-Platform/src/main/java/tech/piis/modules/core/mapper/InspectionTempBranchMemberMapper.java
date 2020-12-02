package tech.piis.modules.core.mapper;

import tech.piis.modules.core.domain.po.InspectionTempBranchMemberPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import java.util.List;
/**
 * 临时支部成员 Mapper接口
 * 
 * @author Tuzki
 * @date 2020-11-23
 */
public interface InspectionTempBranchMemberMapper extends BaseMapper<InspectionTempBranchMemberPO> {
    /**
     * 批量新增临时支部成员
     * @param memberList
     */
    void insertBatch(List<InspectionTempBranchMemberPO> memberList);
}
