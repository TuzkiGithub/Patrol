package tech.piis.modules.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * 巡视附件 Mapper接口
 *
 * @author Tuzki
 * @date 2020-09-14
 */
public interface PiisDocumentMapper extends BaseMapper<PiisDocumentPO> {

    int updateBatch(List<PiisDocumentPO> documents) throws BaseException;
}
