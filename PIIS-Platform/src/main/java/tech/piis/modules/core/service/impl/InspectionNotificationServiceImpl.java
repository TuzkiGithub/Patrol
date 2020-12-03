package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.modules.core.domain.po.InspectionNotificationPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.mapper.InspectionNotificationMapper;
import tech.piis.modules.core.service.IInspectionNotificationService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 情况通报 Service业务层处理
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@Transactional
@Service
public class InspectionNotificationServiceImpl implements IInspectionNotificationService {
    @Autowired
    private InspectionNotificationMapper inspectionNotificationMapper;

    @Autowired
    private IPiisDocumentService documentService;


    /**
     * 查询情况通报 列表
     *
     * @param inspectionNotification
     * @return
     * @throws BaseException
     */
    @Override
    public List<InspectionNotificationPO> selectInspectionNotificationList(InspectionNotificationPO inspectionNotification) throws BaseException {
        QueryWrapper<InspectionNotificationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("PLAN_ID", inspectionNotification.getPlanId());
        List<InspectionNotificationPO> inspectionNotificationList = inspectionNotificationMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(inspectionNotificationList)) {
            inspectionNotificationList.forEach(inspectionNotificationPO -> {
                List<PiisDocumentPO> documents = documentService.getFileListByBizId("InspectionNotification" + inspectionNotificationPO.getNotificationId());
                inspectionNotificationPO.setDocuments(documents);
            });
        }
        return inspectionNotificationList;
    }

    /**
     * 新增情况通报
     *
     * @param inspectionNotification
     * @return
     * @throws BaseException
     */
    @Override
    public int save(InspectionNotificationPO inspectionNotification) throws BaseException {
        int result = inspectionNotificationMapper.insert(inspectionNotification);
        Object bizId = inspectionNotification.getNotificationId();
        List<PiisDocumentPO> documents = inspectionNotification.getDocuments();
        documents.forEach(document -> document.setOperationType(INSERT));
        documentService.updateDocumentBatch(documents, "InspectionNotification" + bizId);
        return result;
    }

    /**
     * 根据ID修改情况通报
     *
     * @param inspectionNotification
     * @return
     * @throws BaseException
     */
    @Override
    public int update(InspectionNotificationPO inspectionNotification) throws BaseException {
        Object bizId = inspectionNotification.getNotificationId();
        documentService.updateDocumentBatch(inspectionNotification.getDocuments(), "InspectionNotification" + bizId);
        return inspectionNotificationMapper.updateById(inspectionNotification);
    }

    /**
     * 根据ID批量删除情况通报
     *
     * @param notificationIds 情况通报编号
     * @return
     */
    @Override
    public int deleteByInspectionNotificationIds(String[] notificationIds) {
        List<String> list = Arrays.asList(notificationIds);
        return inspectionNotificationMapper.deleteBatchIds(list);
    }


    /**
     * 统计总数
     *
     * @return
     */
    @Override
    public int count() {
        return inspectionNotificationMapper.selectCount(null);
    }
}
