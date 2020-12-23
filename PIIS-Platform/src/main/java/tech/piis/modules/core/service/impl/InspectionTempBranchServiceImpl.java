package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.IdUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.modules.core.domain.po.InspectionTempBranchMemberPO;
import tech.piis.modules.core.domain.po.InspectionTempBranchPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionTempBranchMapper;
import tech.piis.modules.core.mapper.InspectionTempBranchMemberMapper;
import tech.piis.modules.core.service.IInspectionTempBranchService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private InspectionTempBranchMemberMapper inspectionTempBranchMemberMapper;

    @Autowired
    private IPiisDocumentService documentService;

    private static final String TEMP_BRANCH_BIZ_NAME = "TempBranch";
    private static final String TEMP_BRANCH_MEMBER_BIZ_NAME = "TempBranchMember";

    /**
     * 与前端接口定义文件类型
     */
    private static final Map<Long, Long> TO_TEMP_MAP = new HashMap<>();
    private static final Map<Long, Long> TO_FILE_MAP = new HashMap<>();

    static {
        TO_FILE_MAP.put(1L, FileEnum.TEMP_OTHER_FILE_ONE.getCode());
        TO_FILE_MAP.put(2L, FileEnum.TEMP_OTHER_FILE_TWO.getCode());
    }

    static {
        TO_TEMP_MAP.put(FileEnum.TEMP_OTHER_FILE_ONE.getCode(), 1L);
        TO_TEMP_MAP.put(FileEnum.TEMP_OTHER_FILE_TWO.getCode(), 2L);
    }


    /**
     * 查询临时支部
     *
     * @param inspectionTempBranch
     * @return
     * @throws BaseException
     */
    @Override
    public InspectionTempBranchPO selectInspectionTempBranchList(InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        InspectionTempBranchPO tempBranch = inspectionTempBranchMapper.selectInspectionTempBranch(inspectionTempBranch);
        if (null != tempBranch) {
            List<PiisDocumentPO> documents = documentService.getFileListByBizId(TEMP_BRANCH_BIZ_NAME + tempBranch.getTempBranchId());
            BizUtils.convertFileDict(documents, TO_TEMP_MAP);
            tempBranch.setDocuments(documents);
        }
        return tempBranch;
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
        String tempBranchId = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionTempBranch.setTempBranchId(tempBranchId);
        List<InspectionTempBranchMemberPO> tempBranchMemberList = inspectionTempBranch.getTempBranchMemberList();
        int result = inspectionTempBranchMapper.insert(inspectionTempBranch.setTempBranchMemberList(null));
        List<PiisDocumentPO> documents = inspectionTempBranch.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        BizUtils.convertFileDict(documents, TO_FILE_MAP);
        documentService.updateDocumentBatch(documents, TEMP_BRANCH_BIZ_NAME + tempBranchId);

        if (!CollectionUtils.isEmpty(tempBranchMemberList)) {
            tempBranchMemberList.forEach(var -> var.setTempBranchId(tempBranchId));
        }
        //批量新增具体党支部，以及文件
        inspectionTempBranchMemberMapper.insertBatch(tempBranchMemberList);
        if (!CollectionUtils.isEmpty(tempBranchMemberList)) {
            tempBranchMemberList.forEach(var -> {
                List<PiisDocumentPO> documentPOS = var.getDocuments();
                documentPOS.forEach(documentPO -> documentPO.setOperationType(INSERT)
                        .setFileDictId(FileEnum.PIIS_TEMP_BRANCH_FILE.getCode()));
                documentService.updateDocumentBatch(documentPOS, TEMP_BRANCH_MEMBER_BIZ_NAME + var.getTempBranchMemberId());
            });
        }
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
        List<InspectionTempBranchMemberPO> tempBranchMemberList = inspectionTempBranch.getTempBranchMemberList();
        int result = inspectionTempBranchMapper.updateById(inspectionTempBranch.setTempBranchMemberList(null));
        String tempBranchId = inspectionTempBranch.getTempBranchId();
        BizUtils.convertFileDict(inspectionTempBranch.getDocuments(), TO_FILE_MAP);
        documentService.updateDocumentBatch(inspectionTempBranch.getDocuments(), TEMP_BRANCH_BIZ_NAME + tempBranchId);

        //修改具体党支部，以及文件：先删除后新增
        QueryWrapper<InspectionTempBranchMemberPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("TEMP_BRANCH_ID", tempBranchId);
        inspectionTempBranchMemberMapper.delete(queryWrapper);

        //批量新增具体党支部，以及文件
        if (!CollectionUtils.isEmpty(tempBranchMemberList)) {
            tempBranchMemberList.forEach(var -> var.setTempBranchId(tempBranchId));
        }
        inspectionTempBranchMemberMapper.insertBatch(tempBranchMemberList);
        if (!CollectionUtils.isEmpty(tempBranchMemberList)) {
            tempBranchMemberList.forEach(var -> {
                List<PiisDocumentPO> documentPOS = var.getDocuments();
                documentPOS.forEach(documentPO -> documentPO.setOperationType(INSERT)
                        .setFileDictId(FileEnum.PIIS_TEMP_BRANCH_FILE.getCode()));
                documentService.updateDocumentBatch(documentPOS, TEMP_BRANCH_MEMBER_BIZ_NAME + var.getTempBranchMemberId());
            });
        }
        return result;
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
