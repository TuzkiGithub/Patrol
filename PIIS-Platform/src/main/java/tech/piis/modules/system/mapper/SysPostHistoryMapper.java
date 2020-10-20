package tech.piis.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import tech.piis.modules.system.domain.SysPostHistory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.system.mapper
 * User: Tuzki
 * Date: 2020/10/14
 * Time: 14:45
 * Description:
 */
public interface SysPostHistoryMapper extends BaseMapper<SysPostHistory> {

    int insertPostHistoryBatch(List<SysPostHistory> postHistoryList);
}
