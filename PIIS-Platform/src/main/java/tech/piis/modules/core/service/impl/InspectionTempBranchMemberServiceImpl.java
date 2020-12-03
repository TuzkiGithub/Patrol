package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

import  tech.piis.modules.core.mapper.InspectionTempBranchMemberMapper;
import  tech.piis.modules.core.domain.po.InspectionTempBranchMemberPO;
import  tech.piis.modules.core.service.IInspectionTempBranchMemberService;

/**
 * 临时支部成员 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionTempBranchMemberServiceImpl implements IInspectionTempBranchMemberService {
    @Autowired
    private InspectionTempBranchMemberMapper inspectionTempBranchMemberMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 查询临时支部成员 列表
     * @param inspectionTempBranchMember
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionTempBranchMemberPO> selectInspectionTempBranchMemberList(InspectionTempBranchMemberPO inspectionTempBranchMember) throws BaseException {
        QueryWrapper<InspectionTempBranchMemberPO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("units_id", inspectionTempBranchMember.getUnitsId());
//        queryWrapper.eq("plan_id", inspectionTempBranchMember.getPlanId());
        return inspectionTempBranchMemberMapper.selectList(queryWrapper);
    }

    /**
     * 新增临时支部成员 
     * @param inspectionTempBranchMember
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionTempBranchMemberPO inspectionTempBranchMember) throws BaseException {
        int result = inspectionTempBranchMemberMapper.insert(inspectionTempBranchMember);
//        List<PiisDocumentPO> documents = inspectionTempBranchMember.getDocuments();
//        documents.forEach(document -> document.setOperationType(INSERT));
//        Object bizId = null;
//        documentService.updateDocumentBatch(documents, "InspectionTempBranchMember" + bizId, null);
        return result;
    }

    /**
     * 根据ID修改临时支部成员 
     * @param inspectionTempBranchMember
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionTempBranchMemberPO inspectionTempBranchMember) throws BaseException {
//        Object bizId = null;
//        documentService.updateDocumentBatch(inspectionTempBranchMember.getDocuments(), "InspectionTempBranchMember" + bizId, null);
        return inspectionTempBranchMemberMapper.updateById(inspectionTempBranchMember);
    }

    /**
     * 根据ID批量删除临时支部成员 
     * @param tempBranchMemberIds 临时支部成员 编号
     *
     * @return
     */
    @Override
    public int deleteByInspectionTempBranchMemberIds(Long[]tempBranchMemberIds) {
        List<Long> list = Arrays.asList(tempBranchMemberIds);
        return inspectionTempBranchMemberMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return inspectionTempBranchMemberMapper.selectCount(null);
    }
}
