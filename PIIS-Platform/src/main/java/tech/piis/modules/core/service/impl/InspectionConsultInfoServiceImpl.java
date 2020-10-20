package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IPiisDocumentService;
import org.springframework.util.CollectionUtils;
import tech.piis.framework.utils.BizUtils;
import java.util.List;
import java.util.Arrays;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.file.FileUploadUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;
import tech.piis.modules.core.mapper.InspectionConsultInfoMapper;
import tech.piis.modules.core.domain.po.InspectionConsultInfoPO;
import tech.piis.modules.core.service.IInspectionConsultInfoService;

/**
 * 查阅资料Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionConsultInfoServiceImpl implements IInspectionConsultInfoService {
    @Autowired
    private InspectionConsultInfoMapper inspectionConsultInfoMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    /**
     * 统计巡视方案下被巡视单位InspectionConsultInfo次数
     * @param planId 巡视计划ID
     *
     */
    public List<UnitsBizCountVO> selectInspectionConsultInfoCount(String planId) throws BaseException {
        return inspectionConsultInfoMapper.selectInspectionConsultInfoCount(planId);
    }

    /**
     * 查询查阅资料列表
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    public List<InspectionConsultInfoPO> selectInspectionConsultInfoList(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        QueryWrapper<InspectionConsultInfoPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionConsultInfo.getUnitsId());
        queryWrapper.eq("plan_id", inspectionConsultInfo.getPlanId());
        return inspectionConsultInfoMapper.selectList(queryWrapper);
    }

    /**
     * 新增查阅资料
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    public int save(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        int result = inspectionConsultInfoMapper.insert(inspectionConsultInfo);
        List<PiisDocumentPO> documents = inspectionConsultInfo.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(null).setFileDictId(null));
            }
        }
        return result;
    }

    /**
     * 根据ID修改查阅资料
     * @param inspectionConsultInfo
     * @return
     * @throws BaseException
     */
    public int update(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        List<PiisDocumentPO> documents = inspectionConsultInfo.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setFileDictId(null)
                                    .setObjectId(String.valueOf(null));
                            documentService.updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            documentService.deleteDocumentById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(filePath, baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return inspectionConsultInfoMapper.updateById(inspectionConsultInfo);
    }

    /**
     * 根据ID批量删除查阅资料
     * @param consultInfoIds 查阅资料编号
     *
     * @return
     */
    public int deleteByInspectionConsultInfoIds(String[] consultInfoIds) {
        List<String> list = Arrays.asList(consultInfoIds);
        return inspectionConsultInfoMapper.deleteBatchIds(list);
    }
}
