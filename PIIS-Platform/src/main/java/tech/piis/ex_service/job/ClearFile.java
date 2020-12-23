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
 * Description :定时清理无关联的文件
 *
 * @author : chenhui@xvco.com
 */
@Slf4j
@Component("ClearFile")
@Transactional
public class ClearFile {

    @Autowired
    private PiisDocumentMapper documentMapper;

    public void clearFile() {
        QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("OBJECT_ID", null);
        List<PiisDocumentPO> piisDocumentPOS = documentMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(piisDocumentPOS)) {
            List<Long> docIdList = new ArrayList<>();
            piisDocumentPOS.forEach(documentPO -> {
                docIdList.add(documentPO.getPiisDocId());
                String filePath = documentPO.getFilePath();
                FileUploadUtils.deleteServerFile(filePath);
            });
            documentMapper.deleteBatchIds(docIdList);
        }
    }

}
