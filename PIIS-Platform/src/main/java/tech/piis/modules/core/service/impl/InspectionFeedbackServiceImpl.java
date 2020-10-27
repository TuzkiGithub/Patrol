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

import  tech.piis.modules.core.mapper.InspectionFeedbackMapper;
import  tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import  tech.piis.modules.core.service.IInspectionFeedbackService;

/**
 * 反馈意见 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Transactional
@Service
public class InspectionFeedbackServiceImpl implements IInspectionFeedbackService {
    @Autowired
    private InspectionFeedbackMapper inspectionFeedbackMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionFeedback次数
     * @param planId 巡视计划ID
     *
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionFeedbackCount(String planId) throws BaseException {
        return inspectionFeedbackMapper.selectInspectionFeedbackCount(planId);
    }

    /**
     * 查询反馈意见 列表
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionFeedbackPO> selectInspectionFeedbackList(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        QueryWrapper<InspectionFeedbackPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionFeedback.getUnitsId());
        queryWrapper.eq("plan_id", inspectionFeedback.getPlanId());
        return inspectionFeedbackMapper.selectList(queryWrapper);
    }

    /**
     * 新增反馈意见 
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        int result = inspectionFeedbackMapper.insert(inspectionFeedback);
        List<PiisDocumentPO> documents = inspectionFeedback.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = null;
        documentService.updateDocumentBatch(documents, "InspectionFeedback" + bizId, null);
        return result;
    }

    /**
     * 根据ID修改反馈意见 
     * @param inspectionFeedback
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        Object bizId = null;
        documentService.updateDocumentBatch(inspectionFeedback.getDocuments(), "InspectionFeedback" + bizId, null);
        return inspectionFeedbackMapper.updateById(inspectionFeedback);
    }

    /**
     * 根据ID批量删除反馈意见 
     * @param feedbackIds 反馈意见 编号
     *
     * @return
     */
    @Override
    public int deleteByInspectionFeedbackIds(String[]feedbackIds) {
        List<String> list = Arrays.asList(feedbackIds);
        return inspectionFeedbackMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return inspectionFeedbackMapper.selectCount(null);
    }
}
