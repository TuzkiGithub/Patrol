package tech.piis.ex_service.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.PiisDocumentMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName : ClearFile
 * Package : tech.piis.ex_service.job
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Component("ClearFile")
@Transactional
public class ClearFile {

    @Autowired
    private PiisDocumentMapper documentMapper;

    public void clearFile() throws Exception {
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("object_id", "");
        List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(piisDocumentPOS)) {
            List<Long> ids = new ArrayList<>();
            piisDocumentPOS.forEach(piisDocumentPO -> {
                ids.add(piisDocumentPO.getPiisDocId());
                String filePath = piisDocumentPO.getFilePath();
                FileUploadUtils.deleteServerFile(filePath);
            });
            documentMapper.deleteBatchIds(ids);
        }
    }

}
