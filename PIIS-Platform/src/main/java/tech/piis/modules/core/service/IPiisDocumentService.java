package tech.piis.modules.core.service;


import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * 巡视附件 Service接口
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
public interface IPiisDocumentService {

    /**
     * 获取巡视动员文件
     * @param bizId 业务ID
     * @return
     * @throws Exception
     */
    List<PiisDocumentPO> getFileListByBizId(String bizId) throws BaseException;

    /**
     * 获取巡视动员文件
     * @param bizIds 业务ID
     * @return
     * @throws Exception
     */
    List<PiisDocumentPO> getFileListByBizIds(List<String> bizIds) throws BaseException;

    /**
     * 根据ID更新文件对象
     * @param documentPO
     * @return
     * @throws Exception
     */
    int updateDocumentById(PiisDocumentPO documentPO) throws BaseException;

    /**
     * 新增文件对象
     * @param documentPO
     * @return
     * @throws Exception
     */
    int saveDocument(PiisDocumentPO documentPO) throws BaseException;

    /**
     * 删除文件
     * @param docId
     * @return
     */
    int deleteDocumentById(Long docId);
}
