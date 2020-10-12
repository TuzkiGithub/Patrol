package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.common.utils.DateUtils;
import tech.piis.framework.utils.SecurityUtils;
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
     * 获取巡视动员文件
     *
     * @param bizId 业务ID
     * @return
     * @throws Exception
     */
    @Override
    public List<PiisDocumentPO> getFileListByBizId(String bizId) throws Exception {
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("OBJECT_ID", bizId);
        return piisDocumentMapper.selectList(queryWrapper);
    }

    /**
     * 获取巡视动员文件
     *
     * @param bizIds 业务ID
     * @return
     * @throws Exception
     */
    @Override
    public List<PiisDocumentPO> getFileListByBizIds(List<String> bizIds) throws Exception {
        return piisDocumentMapper.selectBatchIds(bizIds);
    }

    /**
     * 根据ID更新文件对象
     *
     * @param documentPO
     * @return
     * @throws Exception
     */
    @Override
    public int updateDocumentById(PiisDocumentPO documentPO) throws Exception {
        documentPO.setUpdatedBy(SecurityUtils.getUsername());
        documentPO.setUpdatedTime(DateUtils.getNowDate());
        return piisDocumentMapper.updateById(documentPO);
    }

    /**
     * 新增文件对象
     *
     * @param documentPO
     * @return
     * @throws Exception
     */
    @Override
    public int saveDocument(PiisDocumentPO documentPO) throws Exception {
        documentPO.setCreatedBy(SecurityUtils.getUsername());
        documentPO.setCreatedTime(DateUtils.getNowDate());
        return piisDocumentMapper.insert(documentPO);
    }

}
