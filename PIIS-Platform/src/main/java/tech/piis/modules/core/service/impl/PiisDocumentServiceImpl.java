package tech.piis.modules.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.piis.modules.core.mapper.PiisDocumentMapper;
import tech.piis.modules.core.service.IPiisDocumentService;

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


}
