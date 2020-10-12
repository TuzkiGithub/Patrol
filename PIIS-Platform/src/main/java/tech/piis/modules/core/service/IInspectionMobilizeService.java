package tech.piis.modules.core.service;

import org.springframework.web.multipart.MultipartFile;
import tech.piis.modules.core.domain.po.InspectionMobilizePO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.service
 * User: Tuzki
 * Date: 2020/9/27
 * Time: 9:04
 * Description:
 */
public interface IInspectionMobilizeService {

    /**
     * 查询巡视动员信息
     * @param inspectionMobilize
     * @return
     */
    InspectionMobilizePO findMobilize(InspectionMobilizePO inspectionMobilize) throws Exception;

    /**
     * 新增巡视动员信息
     * @param inspectionMobilize
     * @return
     * @throws Exception
     */
    int saveInspectionMobilize(InspectionMobilizePO inspectionMobilize) throws Exception;

    /**
     * 修改巡视动员信息
     * @param inspectionMobilize
     * @return
     * @throws Exception
     */
    int updateInspectionMobilize(InspectionMobilizePO inspectionMobilize) throws Exception;

    /**
     * 根据巡视动员ID查询文件列表
     * @param mobilizedId
     * @return
     * @throws Exception
     */
    List<PiisDocumentPO> getFileByMobilizeId(String mobilizedId) throws Exception;
}
