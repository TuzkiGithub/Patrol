package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.InspectionSpecialReportPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionSpecialReportMapper;
import tech.piis.modules.core.service.IInspectionSpecialReportService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * SPECIAL_REPORT Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-12
 */
@Service
public class InspectionSpecialReportServiceImpl implements IInspectionSpecialReportService {
    @Autowired
    private InspectionSpecialReportMapper inspectionSpecialReportMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;


    /**
     * 统计巡视方案下被巡视单位的听取报告次数
     *
     * @param planId
     * @return
     */
    @Override
    public List<UnitsBizCountVO> selectSpecialReportCount(String planId) {
        return inspectionSpecialReportMapper.selectSpecialReportCount(planId);
    }

    @Override
    public List<InspectionSpecialReportPO> selectSpecialReport(InspectionSpecialReportPO inspectionSpecialReport) {
        QueryWrapper<InspectionSpecialReportPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionSpecialReport.getUnitsId());
        queryWrapper.eq("plan_id", inspectionSpecialReport.getPlanId());
        return inspectionSpecialReportMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public int update(InspectionSpecialReportPO inspectionSpecialReport) throws Exception {
        List<PiisDocumentPO> documents = inspectionSpecialReport.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setFileDictId(FileEnum.SPECIAL_REPORT_FILE.getCode())
                                    .setObjectId(String.valueOf(inspectionSpecialReport.getSpecialReportId()));
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
        return inspectionSpecialReportMapper.updateById(inspectionSpecialReport);
    }

    @Override
    @Transactional
    public int save(InspectionSpecialReportPO inspectionSpecialReport) throws Exception {
        int result = inspectionSpecialReportMapper.insert(inspectionSpecialReport);
        List<PiisDocumentPO> documents = inspectionSpecialReport.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(String.valueOf(inspectionSpecialReport.getSpecialReportId())).setFileDictId(FileEnum.SPECIAL_REPORT_FILE.getCode()));
            }
        }
        return result;
    }

    @Override
    public int deleteBySpecialReportIds(Long[] specialReportIds) {
        List<Long> list = Arrays.asList(specialReportIds);
        return inspectionSpecialReportMapper.deleteBatchIds(list);
    }
}
