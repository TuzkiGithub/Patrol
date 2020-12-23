package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionLearnTrainPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionLearnTrainMapper;
import tech.piis.modules.core.service.IInspectionLearnTrainService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 学习培训 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionLearnTrainServiceImpl implements IInspectionLearnTrainService {
    @Autowired
    private InspectionLearnTrainMapper inspectionLearnTrainMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 查询学习培训 列表
     *
     * @param inspectionLearnTrain
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionLearnTrainPO> selectInspectionLearnTrainList(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException {
        QueryWrapper<InspectionLearnTrainPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionLearnTrain.getPlanId());
        return inspectionLearnTrainMapper.selectList(queryWrapper);
    }

    /**
     * 新增学习培训
     *
     * @param inspectionLearnTrain
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException {
        int result = inspectionLearnTrainMapper.insert(inspectionLearnTrain);
        List<PiisDocumentPO> documents = inspectionLearnTrain.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        Object bizId = inspectionLearnTrain.getLearnTrainId();
        documentService.updateDocumentBatch(documents, "LearnTrain" + bizId);
        return result;
    }

    /**
     * 根据ID修改学习培训
     *
     * @param inspectionLearnTrain
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException {
        Object bizId = inspectionLearnTrain.getLearnTrainId();
        documentService.updateDocumentBatch(inspectionLearnTrain.getDocuments(), "LearnTrain" + bizId);
        return inspectionLearnTrainMapper.updateById(inspectionLearnTrain);
    }

    /**
     * 根据ID批量删除学习培训
     *
     * @param learnTrainIds 学习培训 编号
     * @return
     */
    @Override
    public int deleteByInspectionLearnTrainIds(Long[] learnTrainIds) {
        List<Long> list = Arrays.asList(learnTrainIds);
        return inspectionLearnTrainMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionLearnTrainMapper.selectCount(null);
    }
}
