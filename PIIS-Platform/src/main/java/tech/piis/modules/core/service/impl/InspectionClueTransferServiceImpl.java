package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

import  tech.piis.modules.core.mapper.InspectionClueTransferMapper;
import  tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import  tech.piis.modules.core.service.IInspectionClueTransferService;

/**
 * 线索移交 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Transactional
@Service
public class InspectionClueTransferServiceImpl implements IInspectionClueTransferService {
    @Autowired
    private InspectionClueTransferMapper inspectionClueTransferMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionClueTransfer次数
     * @param planId 巡视计划ID
     *
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionClueTransferCount(String planId) throws BaseException {
        return inspectionClueTransferMapper.selectInspectionClueTransferCount(planId);
    }

    /**
     * 查询线索移交 列表
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionClueTransferPO> selectInspectionClueTransferList(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        QueryWrapper<InspectionClueTransferPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionClueTransfer.getUnitsId());
        queryWrapper.eq("plan_id", inspectionClueTransfer.getPlanId());
        return inspectionClueTransferMapper.selectList(queryWrapper);
    }

    /**
     * 新增线索移交 
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        int result = inspectionClueTransferMapper.insert(inspectionClueTransfer);
        List<PiisDocumentPO> documents = inspectionClueTransfer.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = null;
        documentService.updateDocumentBatch(documents, "InspectionClueTransfer" + bizId, null);
        return result;
    }

    /**
     * 根据ID修改线索移交 
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        Object bizId = null;
        documentService.updateDocumentBatch(inspectionClueTransfer.getDocuments(), "InspectionClueTransfer" + bizId, null);
        return inspectionClueTransferMapper.updateById(inspectionClueTransfer);
    }

    /**
     * 根据ID批量删除线索移交 
     * @param clueTransferIds 线索移交 编号
     *
     * @return
     */
    @Override
    public int deleteByInspectionClueTransferIds(String[]clueTransferIds) {
        List<String> list = Arrays.asList(clueTransferIds);
        return inspectionClueTransferMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return inspectionClueTransferMapper.selectCount(null);
    }
}
