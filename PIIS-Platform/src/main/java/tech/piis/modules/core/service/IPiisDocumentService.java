package tech.piis.modules.core.service;


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
     * 获取巡视方案文件
     * @param documentPO
     * @return
     * @throws Exception
     */
    List<PiisDocumentPO> getPlanFileList(PiisDocumentPO documentPO) throws Exception;
}
