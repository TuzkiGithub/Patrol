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

import  tech.piis.modules.core.mapper.InspectionIndividualTalkMapper;
import  tech.piis.modules.core.domain.po.InspectionIndividualTalkPO;
import  tech.piis.modules.core.service.IInspectionIndividualTalkService;

/**
 * 个别谈话 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@Transactional
@Service
public class InspectionIndividualTalkServiceImpl implements IInspectionIndividualTalkService {
    @Autowired
    private InspectionIndividualTalkMapper inspectionIndividualTalkMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 统计巡视方案下被巡视单位InspectionIndividualTalk次数
     * @param planId 巡视计划ID
     *
     */
    @Override
    public List<UnitsBizCountVO> selectInspectionIndividualTalkCount(String planId) throws BaseException {
        return inspectionIndividualTalkMapper.selectInspectionIndividualTalkCount(planId);
    }

    /**
     * 查询个别谈话 列表
     * @param inspectionIndividualTalk
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionIndividualTalkPO> selectInspectionIndividualTalkList(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException {
        QueryWrapper<InspectionIndividualTalkPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionIndividualTalk.getUnitsId());
        queryWrapper.eq("plan_id", inspectionIndividualTalk.getPlanId());
        queryWrapper.orderByDesc("created_time");
        return inspectionIndividualTalkMapper.selectList(queryWrapper);
    }

    /**
     * 新增个别谈话 
     * @param inspectionIndividualTalk
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException {
        int result = inspectionIndividualTalkMapper.insert(inspectionIndividualTalk);
        List<PiisDocumentPO> documents = inspectionIndividualTalk.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = null;
        documentService.updateDocumentBatch(documents, "InspectionIndividualTalk" + bizId, null);
        return result;
    }

    /**
     * 根据ID修改个别谈话 
     * @param inspectionIndividualTalk
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException {
        Object bizId = null;
        documentService.updateDocumentBatch(inspectionIndividualTalk.getDocuments(), "InspectionIndividualTalk" + bizId, null);
        return inspectionIndividualTalkMapper.updateById(inspectionIndividualTalk);
    }

    /**
     * 根据ID批量删除个别谈话 
     * @param individualTalkIds 个别谈话 编号
     *
     * @return
     */
    @Override
    public int deleteByInspectionIndividualTalkIds(Long[]individualTalkIds) {
        List<Long> list = Arrays.asList(individualTalkIds);
        return inspectionIndividualTalkMapper.deleteBatchIds(list);
    }


    /**
    * 统计总数
    * @return
    */
    @Override
    public int count() {
        return inspectionIndividualTalkMapper.selectCount(null);
    }
}
