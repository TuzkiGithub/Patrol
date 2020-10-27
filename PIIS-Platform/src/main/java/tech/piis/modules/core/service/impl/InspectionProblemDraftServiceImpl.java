package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionProblemDraftPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionProblemDraftMapper;
import tech.piis.modules.core.service.IInspectionProblemDraftService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 问题底稿 Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-27
 */
@Transactional
@Service
public class InspectionProblemDraftServiceImpl implements IInspectionProblemDraftService {
    @Autowired
    private InspectionProblemDraftMapper inspectionProblemDraftMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionProblemDraft次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionProblemDraftCount(String planId) throws BaseException {
        return inspectionProblemDraftMapper.selectInspectionProblemDraftCount(planId);
    }

    /**
     * 查询问题底稿 列表
     *
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionProblemDraftPO> selectInspectionProblemDraftList(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        QueryWrapper<InspectionProblemDraftPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionProblemDraft.getUnitsId());
        queryWrapper.eq("plan_id", inspectionProblemDraft.getPlanId());
        return inspectionProblemDraftMapper.selectList(queryWrapper);
    }

    /**
     * 新增问题底稿
     *
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        int result = inspectionProblemDraftMapper.insert(inspectionProblemDraft);
        List<PiisDocumentPO> documents = inspectionProblemDraft.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionProblemDraft.getProblemDraftId();
        documentService.updateDocumentBatch(documents, "InspectionProblemDraft" + bizId);
        return result;
    }

    /**
     * 根据ID修改问题底稿
     *
     * @param inspectionProblemDraft
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionProblemDraftPO inspectionProblemDraft) throws BaseException {
        Object bizId = inspectionProblemDraft.getProblemDraftId();
        documentService.updateDocumentBatch(inspectionProblemDraft.getDocuments(), "InspectionProblemDraft" + bizId);
        return inspectionProblemDraftMapper.updateById(inspectionProblemDraft);
    }

    /**
     * 根据ID批量删除问题底稿
     *
     * @param problemDraftIds 问题底稿 编号
     * @return
     */
    @Override
    public int deleteByInspectionProblemDraftIds(Long[] problemDraftIds) {
        List<Long> list = Arrays.asList(problemDraftIds);
        return inspectionProblemDraftMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionProblemDraftMapper.selectCount(null);
    }
}