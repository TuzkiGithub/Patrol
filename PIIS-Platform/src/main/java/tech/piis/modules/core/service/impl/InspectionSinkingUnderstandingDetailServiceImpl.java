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
import tech.piis.modules.core.mapper.InspectionSinkingUnderstandingDetailMapper;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingDetailPO;
import tech.piis.modules.core.service.IInspectionSinkingUnderstandingDetailService;

/**
 * 下沉了解详情Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionSinkingUnderstandingDetailServiceImpl implements IInspectionSinkingUnderstandingDetailService {
    @Autowired
    private InspectionSinkingUnderstandingDetailMapper inspectionSinkingUnderstandingDetailMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstandingDetail次数
     * @param planId 巡视计划ID
     *
     */
    public List<UnitsBizCountVO> selectInspectionSinkingUnderstandingDetailCount(String planId) throws BaseException {
        return inspectionSinkingUnderstandingDetailMapper.selectInspectionSinkingUnderstandingDetailCount(planId);
    }

    /**
     * 查询下沉了解详情列表
     * @param inspectionSinkingUnderstandingDetail
     * @return
     * @throws BaseException
     */
    public List<InspectionSinkingUnderstandingDetailPO> selectInspectionSinkingUnderstandingDetailList(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException {
        QueryWrapper<InspectionSinkingUnderstandingDetailPO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("units_id", inspectionSinkingUnderstandingDetail.getUnitsId());
//        queryWrapper.eq("plan_id", inspectionSinkingUnderstandingDetail.getPlanId());
        return inspectionSinkingUnderstandingDetailMapper.selectList(queryWrapper);
    }

    /**
     * 新增下沉了解详情
     * @param inspectionSinkingUnderstandingDetail
     * @return
     * @throws BaseException
     */
    public int save(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException {
        int result = inspectionSinkingUnderstandingDetailMapper.insert(inspectionSinkingUnderstandingDetail);
        List<PiisDocumentPO> documents = inspectionSinkingUnderstandingDetail.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(null).setFileDictId(null));
            }
        }
        return result;
    }

    /**
     * 根据ID修改下沉了解详情
     * @param inspectionSinkingUnderstandingDetail
     * @return
     * @throws BaseException
     */
    public int update(InspectionSinkingUnderstandingDetailPO inspectionSinkingUnderstandingDetail) throws BaseException {
        List<PiisDocumentPO> documents = inspectionSinkingUnderstandingDetail.getDocuments();
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
                                FileUploadUtils.deleteServerFile(filePath.replace(filePath, baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return inspectionSinkingUnderstandingDetailMapper.updateById(inspectionSinkingUnderstandingDetail);
    }

    /**
     * 根据ID批量删除下沉了解详情
     * @param sinkngUnderstandingDetailIds 下沉了解详情编号
     *
     * @return
     */
    public int deleteByInspectionSinkingUnderstandingDetailIds(Long[] sinkngUnderstandingDetailIds) {
        List<Long> list = Arrays.asList(sinkngUnderstandingDetailIds);
        return inspectionSinkingUnderstandingDetailMapper.deleteBatchIds(list);
    }
}
