package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.modules.core.domain.po.InspectionClueTransferDetailPO;
import tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionClueTransferDetailMapper;
import tech.piis.modules.core.mapper.InspectionClueTransferMapper;
import tech.piis.modules.core.service.IInspectionClueTransferService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;

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
    private InspectionClueTransferDetailMapper clueTransferDetailMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionClueTransfer次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionClueTransferCount(String planId) throws BaseException {
        return inspectionClueTransferMapper.selectInspectionClueTransferCount(planId);
    }

    /**
     * 查询线索移交 列表
     *
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionClueTransferPO> selectInspectionClueTransferList(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        return inspectionClueTransferMapper.selectInspectionClueTransferList(inspectionClueTransfer);
    }

    /**
     * 新增线索移交
     *
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        //设置主键
        String clueTransferId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionClueTransfer.setClueTransferId(clueTransferId);

        //新增线索详情
        List<InspectionClueTransferDetailPO> clueTransferDetailList = inspectionClueTransfer.getClueTransferDetailList();
        if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
            clueTransferDetailList.forEach(var -> {
                var.setClueTransferId(clueTransferId);
                List<PiisDocumentPO> documents = var.getDocuments();
                clueTransferDetailMapper.insert(var.setDocuments(null));
                documents.forEach(document -> {
                            document.setOperationType(INSERT)
                                    .setObjectId("ClueTransferDetail" + var.getClueTransferDetailId());
                            documentService.updateDocumentById(document);
                        }
                );
            });
        }
        return inspectionClueTransferMapper.insert(inspectionClueTransfer.setClueTransferDetailList(null));
    }

    /**
     * 根据ID修改线索移交
     *
     * @param inspectionClueTransfer
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        //修改线索详情
        List<InspectionClueTransferDetailPO> clueTransferDetailList = inspectionClueTransfer.getClueTransferDetailList();
        if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
            clueTransferDetailList.forEach(var -> {
                //查询已存在的文件-线索详情
                QueryWrapper<PiisDocumentPO> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("OBJECT_ID", "ClueTransferDetail" + var.getClueTransferDetailId());
                List<PiisDocumentPO> oldDocuments = documentService.selectDocumentByCondition(queryWrapper);
                List<PiisDocumentPO> documents = var.getDocuments();
                Integer operationType = var.getOperationType();
                var.setClueTransferId(inspectionClueTransfer.getClueTransferId());

                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            List<PiisDocumentPO> tempDoc = var.getDocuments();
                            clueTransferDetailMapper.insert(var.setDocuments(null));
                            tempDoc.forEach(document -> document.setOperationType(INSERT));
                            documentService.updateDocumentBatch(tempDoc, "ClueTransferDetail" + var.getClueTransferDetailId());
                            break;
                        case UPDATE:
                            //标识需要删除的文件
                            /**
                             * oldDoc   1,2,3    1,2,3   1,2
                             * newDoc   3,4      1,3     1,2,3
                             * remove   1,2      2       NULL
                             */
                            if (!CollectionUtils.isEmpty(oldDocuments) && !CollectionUtils.isEmpty(documents)) {
                                oldDocuments.forEach(oldDocument -> {
                                    int count = 0;
                                    for (PiisDocumentPO newDocument : documents) {
                                        if (!oldDocument.getPiisDocId().equals(newDocument.getPiisDocId())) {
                                            count++;
                                        }
                                    }
                                    if (count == documents.size()) {
                                        oldDocument.setOperationType(DELETE);
                                    }
                                });
                            }
                            //文件先更新后删除原有数据
                            if (!CollectionUtils.isEmpty(documents)) {
                                documents.forEach(temp -> temp.setOperationType(INSERT));
                            }
                            documentService.updateDocumentBatch(documents, "ClueTransferDetail" + var.getClueTransferDetailId());
                            documentService.updateDocumentBatch(oldDocuments, "ClueTransferDetail" + var.getClueTransferDetailId());
                            clueTransferDetailMapper.updateById(var.setDocuments(null));
                            break;
                        case DELETE:
                            clueTransferDetailMapper.deleteById(var.getClueTransferDetailId());
                            documents.forEach(document -> document.setOperationType(DELETE));
                            documentService.updateDocumentBatch(documents, "ClueTransferDetail" + var.getClueTransferDetailId());
                            break;
                    }
                }

            });
        }
        return inspectionClueTransferMapper.updateById(inspectionClueTransfer.setClueTransferDetailList(null));
    }

    /**
     * 根据ID批量删除线索移交
     *
     * @param clueTransferIds 线索移交 编号
     * @return
     */
    @Override
    public int deleteByInspectionClueTransferIds(String[] clueTransferIds) {
        List<String> list = Arrays.asList(clueTransferIds);
        list.forEach(clueTransferId -> {
            QueryWrapper<InspectionClueTransferDetailPO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CLUE_TRANSFER_ID", clueTransferId);
            clueTransferDetailMapper.delete(queryWrapper);
        });
        return inspectionClueTransferMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count(InspectionClueTransferPO inspectionClueTransfer) {
        QueryWrapper<InspectionClueTransferPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionClueTransfer.getPlanId());
        queryWrapper.eq("UNITS_ID", inspectionClueTransfer.getUnitsId());
        return inspectionClueTransferMapper.selectCount(queryWrapper);
    }
}
