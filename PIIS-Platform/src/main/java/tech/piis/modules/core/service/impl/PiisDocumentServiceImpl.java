package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

/**
 * 巡视附件 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@Service
public class PiisDocumentServiceImpl implements IPiisDocumentService {

    @Autowired
    private PiisDocumentMapper piisDocumentMapper;


    /**
     * 获取巡视方案文件
     *
     * @param documentPO
     * @return
     * @throws Exception
     */
    @Override
    public List<PiisDocumentPO> getPlanFileList(PiisDocumentPO documentPO) throws Exception {
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        if (null != documentPO) {
            queryWrapper.eq("OBJECT_ID", documentPO.getObjectId());
        }
        return piisDocumentMapper.selectList(queryWrapper);
    }
}
