package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionReportApprovalPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionReportApprovalMapper;
import tech.piis.modules.core.service.IInspectionReportApprovalService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 报请审批 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionReportApprovalServiceImpl implements IInspectionReportApprovalService {
    @Autowired
    private InspectionReportApprovalMapper inspectionReportApprovalMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 查询报请审批 列表
     *
     * @param inspectionReportApproval
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionReportApprovalPO> selectInspectionReportApprovalList(InspectionReportApprovalPO inspectionReportApproval) throws BaseException {
        QueryWrapper<InspectionReportApprovalPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionReportApproval.getPlanId());
        return inspectionReportApprovalMapper.selectList(queryWrapper);
    }

    /**
     * 新增报请审批
     *
     * @param inspectionReportApproval
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionReportApprovalPO inspectionReportApproval) throws BaseException {
        int result = inspectionReportApprovalMapper.insert(inspectionReportApproval);
        List<PiisDocumentPO> documents = inspectionReportApproval.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionReportApproval.getReportApprovalId();
        documentService.updateDocumentBatch(documents, "InspectionReportApproval" + bizId, FileEnum.REPORT_APPROVAL_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改报请审批
     *
     * @param inspectionReportApproval
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionReportApprovalPO inspectionReportApproval) throws BaseException {
        String objectId = "InspectionReportApproval" + inspectionReportApproval.getReportApprovalId();
        List<PiisDocumentPO> documents = inspectionReportApproval.getDocuments();
        documentService.updateDocumentBatch(documents, String.valueOf(objectId));
        return inspectionReportApprovalMapper.updateById(inspectionReportApproval);
    }

    /**
     * 根据ID批量删除报请审批
     *
     * @param reportApprovalIds 报请审批 编号
     * @return
     */
    @Override
    public int deleteByInspectionReportApprovalIds(Long[] reportApprovalIds) {
        List<Long> list = Arrays.asList(reportApprovalIds);
        return inspectionReportApprovalMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionReportApprovalMapper.selectCount(null);
    }
}
