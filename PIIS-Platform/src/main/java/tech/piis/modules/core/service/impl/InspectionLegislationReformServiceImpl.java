package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionLegislationReformPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionLegislationReformMapper;
import tech.piis.modules.core.service.IInspectionLegislationReformService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 立行立改 Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-23
 */
@Transactional
@Service
public class InspectionLegislationReformServiceImpl implements IInspectionLegislationReformService {
    @Autowired
    private InspectionLegislationReformMapper inspectionLegislationReformMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionLegislationReform次数
     *
     * @param planId 巡视计划ID
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionLegislationReformCount(String planId) throws BaseException {
        return inspectionLegislationReformMapper.selectInspectionLegislationReformCount(planId);
    }

    /**
     * 查询立行立改 列表
     *
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionLegislationReformPO> selectInspectionLegislationReformList(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException {
        QueryWrapper<InspectionLegislationReformPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionLegislationReform.getUnitsId());
        queryWrapper.eq("plan_id", inspectionLegislationReform.getPlanId());
        return inspectionLegislationReformMapper.selectList(queryWrapper);
    }

    /**
     * 新增立行立改
     *
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException {
        int result = inspectionLegislationReformMapper.insert(inspectionLegislationReform);
        List<PiisDocumentPO> documents = inspectionLegislationReform.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionLegislationReform.getLegislationReformId();
        documentService.updateDocumentBatch(documents, "InspectionLegislationReform" + bizId, FileEnum.LEGISLATION_REFORM_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改立行立改
     *
     * @param inspectionLegislationReform
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionLegislationReformPO inspectionLegislationReform) throws BaseException {
        Object bizId = inspectionLegislationReform.getLegislationReformId();
        documentService.updateDocumentBatch(inspectionLegislationReform.getDocuments(), "InspectionLegislationReform" + bizId, FileEnum.LEGISLATION_REFORM_FILE.getCode());
        return inspectionLegislationReformMapper.updateById(inspectionLegislationReform);
    }

    /**
     * 根据ID批量删除立行立改
     *
     * @param legislationReformIds 立行立改 编号
     * @return
     */
    @Override
    public int deleteByInspectionLegislationReformIds(Long[] legislationReformIds) {
        List<Long> list = Arrays.asList(legislationReformIds);
        return inspectionLegislationReformMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionLegislationReformMapper.selectCount(null);
    }
}
