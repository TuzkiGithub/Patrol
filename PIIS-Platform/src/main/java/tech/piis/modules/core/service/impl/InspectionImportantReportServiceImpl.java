package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

import  tech.piis.modules.core.mapper.InspectionImportantReportMapper;
import  tech.piis.modules.core.domain.po.InspectionImportantReportPO;
import  tech.piis.modules.core.service.IInspectionImportantReportService;

/**
 * 重要情况专题报告 Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-23
 */
@Transactional
@Service
public class InspectionImportantReportServiceImpl implements IInspectionImportantReportService {
    @Autowired
    private InspectionImportantReportMapper inspectionImportantReportMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionImportantReport次数
     * @param planId 巡视计划ID
     *
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionImportantReportCount(String planId) throws BaseException {
        return inspectionImportantReportMapper.selectInspectionImportantReportCount(planId);
    }

    /**
     * 查询重要情况专题报告 列表
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionImportantReportPO> selectInspectionImportantReportList(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        QueryWrapper<InspectionImportantReportPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionImportantReport.getUnitsId());
        queryWrapper.eq("plan_id", inspectionImportantReport.getPlanId());
        return inspectionImportantReportMapper.selectList(queryWrapper);
    }

    /**
     * 新增重要情况专题报告 
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        int result = inspectionImportantReportMapper.insert(inspectionImportantReport);
        List<PiisDocumentPO> documents = inspectionImportantReport.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionImportantReport.getImportantReportId();
        documentService.updateDocumentBatch(documents, "InspectionImportantReport" + bizId, FileEnum.IMPORTANT_SPORT_FILE.getCode());
        return result;
    }

    /**
     * 根据ID修改重要情况专题报告 
     * @param inspectionImportantReport
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionImportantReportPO inspectionImportantReport) throws BaseException {
        Object bizId = inspectionImportantReport.getImportantReportId();
        documentService.updateDocumentBatch(inspectionImportantReport.getDocuments(), "InspectionImportantReport" + bizId, FileEnum.IMPORTANT_SPORT_FILE.getCode());
        return inspectionImportantReportMapper.updateById(inspectionImportantReport);
    }

    /**
     * 根据ID批量删除重要情况专题报告 
     * @param inspectionImportantReportIds 重要情况专题报告 编号
     *
     * @return
     */
    @Override
    public int deleteByInspectionImportantReportIds(Long[]inspectionImportantReportIds) {
        List<Long> list = Arrays.asList(inspectionImportantReportIds);
        return inspectionImportantReportMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return inspectionImportantReportMapper.selectCount(null);
    }
}
