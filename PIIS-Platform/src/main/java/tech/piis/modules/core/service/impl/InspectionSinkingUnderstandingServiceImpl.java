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
import tech.piis.modules.core.domain.po.InspectionConsultInfoPO;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingDetailPO;
import tech.piis.modules.core.domain.po.InspectionSinkingUnderstandingPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionSinkingUnderstandingDetailMapper;
import tech.piis.modules.core.mapper.InspectionSinkingUnderstandingMapper;
import tech.piis.modules.core.service.IInspectionSinkingUnderstandingService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.*;

/**
 * 下沉了解Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionSinkingUnderstandingServiceImpl implements IInspectionSinkingUnderstandingService {
    @Autowired
    private InspectionSinkingUnderstandingMapper inspectionSinkingUnderstandingMapper;

    @Autowired
    private InspectionSinkingUnderstandingDetailMapper inspectionSinkingUnderstandingDetailMapper;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 统计巡视方案下被巡视单位InspectionSinkingUnderstanding次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionSinkingUnderstandingCount(String planId) throws BaseException {
        return inspectionSinkingUnderstandingMapper.selectInspectionSinkingUnderstandingCount(planId);
    }

    /**
     * 查询下沉了解列表
     *
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public List<InspectionSinkingUnderstandingPO> selectInspectionSinkingUnderstandingList(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        return inspectionSinkingUnderstandingMapper.selectInspectionSinkingUnderstandingList(inspectionSinkingUnderstanding);
    }

    /**
     * 新增下沉了解
     *
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public int save(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        //设置主键
        String id = DateUtils.dateTime() + IdUtils.simpleUUID().substring(0, 6);
        inspectionSinkingUnderstanding.setSinkingUnderstandingId(id);

        List<PiisDocumentPO> documents = inspectionSinkingUnderstanding.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        documentService.updateDocumentBatch(documents, "SinkingUnderstanding" + id, FileEnum.SINKING_OTHER_FILE.getCode());

        //新增下沉了解详情
        List<InspectionSinkingUnderstandingDetailPO> sinkingUnderstandingDetails = inspectionSinkingUnderstanding.getSinkingUnderstandingDetailList();
        if (!CollectionUtils.isEmpty(sinkingUnderstandingDetails)) {
            sinkingUnderstandingDetails.forEach(var -> inspectionSinkingUnderstandingDetailMapper.insert(var.setSinkingUnderstandingId(id)));
        }
        return inspectionSinkingUnderstandingMapper.insert(inspectionSinkingUnderstanding.setSinkingUnderstandingDetailList(null));
    }

    /**
     * 根据ID修改下沉了解
     *
     * @param inspectionSinkingUnderstanding
     * @return
     * @throws BaseException
     */
    public int update(InspectionSinkingUnderstandingPO inspectionSinkingUnderstanding) throws BaseException {
        //更新下沉了解文件
        documentService.updateDocumentBatch(inspectionSinkingUnderstanding.getDocuments(), "SinkingUnderstanding" + inspectionSinkingUnderstanding.getSinkingUnderstandingId(), FileEnum.SINKING_OTHER_FILE.getCode());

        //更新下沉了解详情
        List<InspectionSinkingUnderstandingDetailPO> sinkingUnderstandingDetailList = inspectionSinkingUnderstanding.getSinkingUnderstandingDetailList();
        if (!CollectionUtils.isEmpty(sinkingUnderstandingDetailList)) {
            sinkingUnderstandingDetailList.forEach(var -> var.setSinkingUnderstandingId(inspectionSinkingUnderstanding.getSinkingUnderstandingId()));
            for (InspectionSinkingUnderstandingDetailPO sinkingUnderstandingDetail : sinkingUnderstandingDetailList) {
                Integer operationType = sinkingUnderstandingDetail.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT:
                            inspectionSinkingUnderstandingDetailMapper.insert(sinkingUnderstandingDetail);
                            break;
                        case UPDATE:
                            inspectionSinkingUnderstandingDetailMapper.updateById(sinkingUnderstandingDetail);
                            break;
                        case DELETE:
                            inspectionSinkingUnderstandingDetailMapper.deleteById(sinkingUnderstandingDetail.getSinkingUnderstandingDetailId());
                            break;
                    }
                }
            }
        }
        return inspectionSinkingUnderstandingMapper.updateById(inspectionSinkingUnderstanding.setSinkingUnderstandingDetailList(null));
    }

    /**
     * 根据ID批量删除下沉了解
     *
     * @param sinkingUnderstandingIds 下沉了解编号
     * @return
     */
    public int deleteByInspectionSinkingUnderstandingIds(String[] sinkingUnderstandingIds) {
        List<String> list = Arrays.asList(sinkingUnderstandingIds);
        return inspectionSinkingUnderstandingMapper.deleteBatchIds(list);
    }

    /**
     * 查询总数
     *
     * @return
     */
    @Override
    public int count(Long unitsId) {
        QueryWrapper<InspectionSinkingUnderstandingPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("UNITS_ID", unitsId);
        return inspectionSinkingUnderstandingMapper.selectCount(queryWrapper);
    }
}
