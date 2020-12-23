package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionCommitteeMeetingsPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionCommitteeMeetingsMapper;
import tech.piis.modules.core.service.IInspectionCommitteeMeetingsService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 党委会小组会纪要 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Transactional
@Service
public class InspectionCommitteeMeetingsServiceImpl implements IInspectionCommitteeMeetingsService {
    @Autowired
    private InspectionCommitteeMeetingsMapper inspectionCommitteeMeetingsMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionCommitteeMeetings次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionCommitteeMeetingsCount(String planId) throws BaseException {
        return inspectionCommitteeMeetingsMapper.selectInspectionCommitteeMeetingsCount(planId).stream().sorted(Comparator.comparing(UnitsBizCountVO::getUnitsId).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询党委会小组会纪要 列表
     *
     * @param inspectionCommitteeMeetings
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionCommitteeMeetingsPO> selectInspectionCommitteeMeetingsList(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException {
        QueryWrapper<InspectionCommitteeMeetingsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionCommitteeMeetings.getUnitsId());
        queryWrapper.eq("plan_id", inspectionCommitteeMeetings.getPlanId());
        queryWrapper.orderByDesc("created_time");
        return inspectionCommitteeMeetingsMapper.selectList(queryWrapper);
    }

    /**
     * 新增党委会小组会纪要
     *
     * @param inspectionCommitteeMeetings
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException {
        int result = inspectionCommitteeMeetingsMapper.insert(inspectionCommitteeMeetings);
        List<PiisDocumentPO> documents = inspectionCommitteeMeetings.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionCommitteeMeetings.getCommitteeMeetingsId();
        documentService.updateDocumentBatch(documents, "CommitteeMeetings" + bizId, FileEnum.COMMITTEE_OTHER_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改党委会小组会纪要
     *
     * @param inspectionCommitteeMeetings
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException {
        Object bizId = inspectionCommitteeMeetings.getCommitteeMeetingsId();
        documentService.updateDocumentBatch(inspectionCommitteeMeetings.getDocuments(), "CommitteeMeetings" + bizId, FileEnum.COMMITTEE_OTHER_FILE.getCode());
        return inspectionCommitteeMeetingsMapper.updateById(inspectionCommitteeMeetings);
    }

    /**
     * 根据ID批量删除党委会小组会纪要
     *
     * @param committeeMeetingsIds 党委会小组会纪要 编号
     * @return
     */
    @Override
    public int deleteByInspectionCommitteeMeetingsIds(Long[] committeeMeetingsIds) {
        List<Long> list = Arrays.asList(committeeMeetingsIds);
        return inspectionCommitteeMeetingsMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionCommitteeMeetingsMapper.selectCount(null);
    }
}
