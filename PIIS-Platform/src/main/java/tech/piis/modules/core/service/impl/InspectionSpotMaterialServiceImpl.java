package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionSpotMaterialPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionSpotMaterialMapper;
import tech.piis.modules.core.service.IInspectionSpotMaterialService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 驻场材料 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionSpotMaterialServiceImpl implements IInspectionSpotMaterialService {
    @Autowired
    private InspectionSpotMaterialMapper inspectionSpotMaterialMapper;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询驻场材料 列表
     *
     * @param inspectionSpotMaterial
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionSpotMaterialPO> selectInspectionSpotMaterialList(InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException {
        QueryWrapper<InspectionSpotMaterialPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionSpotMaterial.getPlanId());
        return inspectionSpotMaterialMapper.selectList(queryWrapper);
    }

    /**
     * 新增驻场材料
     *
     * @param inspectionSpotMaterial
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException {
        int result = inspectionSpotMaterialMapper.insert(inspectionSpotMaterial);
        List<PiisDocumentPO> documents = inspectionSpotMaterial.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionSpotMaterial.getSpotMaterialId();
        documentService.updateDocumentBatch(documents, "SpotMaterial" + bizId);
        return result;
    }

    /**
     * 根据ID修改驻场材料
     *
     * @param inspectionSpotMaterial
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException {
        String objectId = "SpotMaterial" + inspectionSpotMaterial.getSpotMaterialId();
        List<PiisDocumentPO> documents = inspectionSpotMaterial.getDocuments();
        documentService.updateDocumentBatch(documents, objectId);
        return inspectionSpotMaterialMapper.updateById(inspectionSpotMaterial);
    }

    /**
     * 根据ID批量删除驻场材料
     *
     * @param inspectionSpotMaterialIds 驻场材料 编号
     * @return
     */
    @Override
    public int deleteByInspectionSpotMaterialIds(Long[] inspectionSpotMaterialIds) {
        List<Long> list = Arrays.asList(inspectionSpotMaterialIds);
        return inspectionSpotMaterialMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionSpotMaterialMapper.selectCount(null);
    }
}
