package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 巡视附件 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@Transactional
@Service
public class PiisDocumentServiceImpl implements IPiisDocumentService {

    @Autowired
    private PiisDocumentMapper piisDocumentMapper;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 获取巡视动员文件
     *
     * @param bizId 业务ID
     * @return
     * @throws BaseException
     */
    @Override
    public List<PiisDocumentPO> getFileListByBizId(String bizId) throws BaseException {
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("OBJECT_ID", bizId);
        return piisDocumentMapper.selectList(queryWrapper);
    }

    /**
     * 获取巡视动员文件
     *
     * @param bizIds 业务ID
     * @return
     * @throws BaseException
     */
    @Override
    public List<PiisDocumentPO> getFileListByBizIds(List<String> bizIds) throws BaseException {
        return piisDocumentMapper.selectBatchIds(bizIds);
    }

    /**
     * 根据ID更新文件对象
     *
     * @param documentPO
     * @return
     * @throws BaseException
     */
    @Override
    public int updateDocumentById(PiisDocumentPO documentPO) throws BaseException {
        documentPO.setUpdatedBy(SecurityUtils.getUsername());
        documentPO.setUpdatedTime(DateUtils.getNowDate());
        return piisDocumentMapper.updateById(documentPO);
    }

    /**
     * 批量更新文件对象
     *
     * @param documents  文件对象
     * @param objectId   业务主键
     * @param fileDictId 文件字典Id
     * @return
     * @throws BaseException
     */
    @Override
    public void updateDocumentBatch(List<PiisDocumentPO> documents, String objectId, Long fileDictId) throws BaseException {
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setFileDictId(fileDictId)
                                    .setObjectId(objectId);
                            BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                            updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            deleteDocumentById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 批量更新文件对象
     *
     * @param documents 文件对象
     * @param objectId  业务主键
     * @return
     * @throws BaseException
     */
    @Override
    public void updateDocumentBatch(List<PiisDocumentPO> documents, String objectId) throws BaseException {
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setObjectId(objectId);
                            BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                            updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            deleteDocumentById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增文件对象
     *
     * @param documentPO
     * @return
     * @throws BaseException
     */
    @Override
    public int saveDocument(PiisDocumentPO documentPO) throws BaseException {
        documentPO.setCreatedBy(SecurityUtils.getUsername());
        documentPO.setCreatedTime(DateUtils.getNowDate());
        return piisDocumentMapper.insert(documentPO);
    }

    /**
     * 删除文件
     *
     * @param docId
     * @return
     */
    @Override
    public int deleteDocumentById(Long docId) {
        return piisDocumentMapper.deleteById(docId);
    }

    /**
     * 删除文件对象以及文件
     *
     * @param docId
     * @param filePath
     * @return
     */
    @Override
    public int deleteDocumentById(Long docId, String filePath) {
        int result = deleteDocumentById(docId);
        if (!StringUtils.isEmpty(filePath)) {
            FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
        }
        return result;
    }

}
