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
import tech.piis.modules.core.mapper.InspectionInvestigationVisitPersonMapper;
import tech.piis.modules.core.domain.po.InspectionInvestigationVisitPersonPO;
import tech.piis.modules.core.service.IInspectionInvestigationVisitPersonService;

/**
 * 调研走访人员Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionInvestigationVisitPersonServiceImpl implements IInspectionInvestigationVisitPersonService {
    @Autowired
    private InspectionInvestigationVisitPersonMapper inspectionInvestigationVisitPersonMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    /**
     * 统计巡视方案下被巡视单位InspectionInvestigationVisitPerson次数
     * @param planId 巡视计划ID
     *
     */
    public List<UnitsBizCountVO> selectInspectionInvestigationVisitPersonCount(String planId) throws BaseException {
        return inspectionInvestigationVisitPersonMapper.selectInspectionInvestigationVisitPersonCount(planId);
    }

    /**
     * 查询调研走访人员列表
     * @param inspectionInvestigationVisitPerson
     * @return
     * @throws BaseException
     */
    public List<InspectionInvestigationVisitPersonPO> selectInspectionInvestigationVisitPersonList(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException {
        QueryWrapper<InspectionInvestigationVisitPersonPO> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("units_id", inspectionInvestigationVisitPerson.getUnitsId());
//        queryWrapper.eq("plan_id", inspectionInvestigationVisitPerson.getPlanId());
        return inspectionInvestigationVisitPersonMapper.selectList(queryWrapper);
    }

    /**
     * 新增调研走访人员
     * @param inspectionInvestigationVisitPerson
     * @return
     * @throws BaseException
     */
    public int save(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException {
        int result = inspectionInvestigationVisitPersonMapper.insert(inspectionInvestigationVisitPerson);
        List<PiisDocumentPO> documents = inspectionInvestigationVisitPerson.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(null).setFileDictId(null));
            }
        }
        return result;
    }

    /**
     * 根据ID修改调研走访人员
     * @param inspectionInvestigationVisitPerson
     * @return
     * @throws BaseException
     */
    public int update(InspectionInvestigationVisitPersonPO inspectionInvestigationVisitPerson) throws BaseException {
        List<PiisDocumentPO> documents = inspectionInvestigationVisitPerson.getDocuments();
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
        return inspectionInvestigationVisitPersonMapper.updateById(inspectionInvestigationVisitPerson);
    }

    /**
     * 根据ID批量删除调研走访人员
     * @param investigationVisitPersonIds 调研走访人员编号
     *
     * @return
     */
    public int deleteByInspectionInvestigationVisitPersonIds(Long[] investigationVisitPersonIds) {
        List<Long> list = Arrays.asList(investigationVisitPersonIds);
        return inspectionInvestigationVisitPersonMapper.deleteBatchIds(list);
    }
}
