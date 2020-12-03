package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionIssuanceNoticePO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionIssuanceNoticeMapper;
import tech.piis.modules.core.service.IInspectionIssuanceNoticeService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 印发通知 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionIssuanceNoticeServiceImpl implements IInspectionIssuanceNoticeService {
    @Autowired
    private InspectionIssuanceNoticeMapper inspectionIssuanceNoticeMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 查询印发通知 列表
     *
     * @param inspectionIssuanceNotice
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionIssuanceNoticePO> selectInspectionIssuanceNoticeList(InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException {
        QueryWrapper<InspectionIssuanceNoticePO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionIssuanceNotice.getPlanId());
        return inspectionIssuanceNoticeMapper.selectList(queryWrapper);
    }

    /**
     * 新增印发通知
     *
     * @param inspectionIssuanceNotice
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException {
        int result = inspectionIssuanceNoticeMapper.insert(inspectionIssuanceNotice);
        List<PiisDocumentPO> documents = inspectionIssuanceNotice.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionIssuanceNotice.getIssuanceNoticeId();
        documentService.updateDocumentBatch(documents, "InspectionIssuanceNotice" + bizId, FileEnum.PRINT_TOPIC_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改印发通知
     *
     * @param inspectionIssuanceNotice
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException {
        String objectId = "InspectionIssuanceNotice" + inspectionIssuanceNotice.getIssuanceNoticeId();
        List<PiisDocumentPO> documents = inspectionIssuanceNotice.getDocuments();
        documentService.updateDocumentBatch(documents, String.valueOf(objectId));
        return inspectionIssuanceNoticeMapper.updateById(inspectionIssuanceNotice);
    }

    /**
     * 根据ID批量删除印发通知
     *
     * @param issuanceNoticeIds 印发通知 编号
     * @return
     */
    @Override
    public int deleteByInspectionIssuanceNoticeIds(Long[] issuanceNoticeIds) {
        List<Long> list = Arrays.asList(issuanceNoticeIds);
        return inspectionIssuanceNoticeMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionIssuanceNoticeMapper.selectCount(null);
    }
}
