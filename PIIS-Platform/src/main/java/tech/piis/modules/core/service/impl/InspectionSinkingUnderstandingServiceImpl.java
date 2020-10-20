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
import tech.piis.modules.core.mapper.InspectionSinkingUnderstandingMapper;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingPO;
import tech.piis.modules.core.service.IInspectionSinkingUnderstandingService;

/**
 * 下沉了解Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionSinkingUnderstandingServiceImpl implements IInspectionSinkingUnderstandingService {
    @Autowired
    private InspectionSinkingUnderstandingMapper inspectionSinkingUnderstandingMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstanding次数
     * @param planId 巡视计划ID
     *
     */
    public List<UnitsBizCountVO> selectInspectionSinkingUnderstandingCount(String planId) throws BaseException {
        return inspectionSinkingUnderstandingMapper.selectInspectionSinkingUnderstandingCount(planId);
    }

    /**
     * 查询下沉了解列表
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public List<InspectionSinkingUnderstandingPO> selectInspectionSinkingUnderstandingList(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        QueryWrapper<InspectionSinkingUnderstandingPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionSinkingUnderstanding.getUnitsId());
        queryWrapper.eq("plan_id", inspectionSinkingUnderstanding.getPlanId());
        return inspectionSinkingUnderstandingMapper.selectList(queryWrapper);
    }

    /**
     * 新增下沉了解
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public int save(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        int result = inspectionSinkingUnderstandingMapper.insert(inspectionSinkingUnderstanding);
        List<PiisDocumentPO> documents = inspectionSinkingUnderstanding.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(null).setFileDictId(null));
            }
        }
        return result;
    }

    /**
     * 根据ID修改下沉了解
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public int update(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        List<PiisDocumentPO> documents = inspectionSinkingUnderstanding.getDocuments();
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
        return inspectionSinkingUnderstandingMapper.updateById(inspectionSinkingUnderstanding);
    }

    /**
     * 根据ID批量删除下沉了解
     * @param sinkngUnderstandingIds 下沉了解编号
     *
     * @return
     */
    public int deleteByInspectionSinkingUnderstandingIds(String[] sinkngUnderstandingIds) {
        List<String> list = Arrays.asList(sinkngUnderstandingIds);
        return inspectionSinkingUnderstandingMapper.deleteBatchIds(list);
    }
}
