package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IPiisDocumentService;
import org.springframework.util.CollectionUtils;
import tech.piis.framework.utils.BizUtils;
import java.util.List;
import java.util.Arrays;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.file.FileUploadUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;
import tech.piis.modules.core.mapper.InspectionConsultInfoDetailMapper;
import tech.piis.modules.core.domain.po.InspectionConsultInfoDetailPO;
import tech.piis.modules.core.service.IInspectionConsultInfoDetailService;

/**
 * 查阅资料详情Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionConsultInfoDetailServiceImpl implements IInspectionConsultInfoDetailService {
    @Autowired
    private InspectionConsultInfoDetailMapper inspectionConsultInfoDetailMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 查询查阅资料详情列表
     * @param inspectionConsultInfoDetail
     * @return
     * @throws BaseException
     */
    public List<InspectionConsultInfoDetailPO> selectInspectionConsultInfoDetailList(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException {
        QueryWrapper<InspectionConsultInfoDetailPO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("units_id", inspectionConsultInfoDetail.getUnitsId());
//        queryWrapper.eq("plan_id", inspectionConsultInfoDetail.getPlanId());
        return inspectionConsultInfoDetailMapper.selectList(queryWrapper);
    }

    /**
     * 新增查阅资料详情
     * @param inspectionConsultInfoDetail
     * @return
     * @throws BaseException
     */
    public int save(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException {
        int result = inspectionConsultInfoDetailMapper.insert(inspectionConsultInfoDetail);
        List<PiisDocumentPO> documents = inspectionConsultInfoDetail.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(null).setFileDictId(null));
            }
        }
        return result;
    }

    /**
     * 根据ID修改查阅资料详情
     * @param inspectionConsultInfoDetail
     * @return
     * @throws BaseException
     */
    public int update(InspectionConsultInfoDetailPO inspectionConsultInfoDetail) throws BaseException {
        List<PiisDocumentPO> documents = inspectionConsultInfoDetail.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setFileDictId(null)
                                    .setObjectId(String.valueOf(null));
                            documentService.updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            documentService.deleteDocumentById(document.getPiisDocId());
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
        return inspectionConsultInfoDetailMapper.updateById(inspectionConsultInfoDetail);
    }

    /**
     * 根据ID批量删除查阅资料详情
     * @param consultInfoDetailIds 查阅资料详情编号
     *
     * @return
     */
    public int deleteByInspectionConsultInfoDetailIds(Long[] consultInfoDetailIds) {
        List<Long> list = Arrays.asList(consultInfoDetailIds);
        return inspectionConsultInfoDetailMapper.deleteBatchIds(list);
    }
}
