package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionTempBranchPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionTempBranchMapper;
import tech.piis.modules.core.service.IInspectionTempBranchService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 临时支部 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionTempBranchServiceImpl implements IInspectionTempBranchService {
    @Autowired
    private InspectionTempBranchMapper inspectionTempBranchMapper;


    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 查询临时支部 列表
     *
     * @param inspectionTempBranch
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionTempBranchPO> selectInspectionTempBranchList(InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        QueryWrapper<InspectionTempBranchPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionTempBranch.getPlanId());
        List<InspectionTempBranchPO> tempBranchList = inspectionTempBranchMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(tempBranchList)) {
            tempBranchList.forEach(inspectionTempBranchPO -> {
                List<PiisDocumentPO> documents = documentService.getFileListByBizId("InspectionTempBranch" + inspectionTempBranchPO.getTempBranchId());
                inspectionTempBranchPO.setDocuments(documents);
            });
        }
        return tempBranchList;
    }

    /**
     * 新增临时支部
     *
     * @param inspectionTempBranch
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        int result = inspectionTempBranchMapper.insert(inspectionTempBranch);
        List<PiisDocumentPO> documents = inspectionTempBranch.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionTempBranch.getTempBranchId();
        documentService.updateDocumentBatch(documents, "InspectionTempBranch" + bizId, FileEnum.PIIS_TEMP_BRANCH_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改临时支部
     *
     * @param inspectionTempBranch
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        Object bizId = inspectionTempBranch.getTempBranchId();
        documentService.updateDocumentBatch(inspectionTempBranch.getDocuments(), "InspectionTempBranch" + bizId, FileEnum.PIIS_TEMP_BRANCH_FILE.getCode());
        return inspectionTempBranchMapper.updateById(inspectionTempBranch);
    }

    /**
     * 根据ID批量删除临时支部
     *
     * @param tempBranchIds 临时支部 编号
     * @return
     */
    @Override
    public int deleteByInspectionTempBranchIds(Long[] tempBranchIds) {
        List<Long> list = Arrays.asList(tempBranchIds);
        return inspectionTempBranchMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionTempBranchMapper.selectCount(null);
    }
}
