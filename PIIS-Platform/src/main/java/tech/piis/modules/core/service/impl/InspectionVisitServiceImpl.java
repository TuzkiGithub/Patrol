package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.InspectionVisitPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionVisitMapper;
import tech.piis.modules.core.service.IInspectionVisitService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 来访Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionVisitServiceImpl implements IInspectionVisitService {
    @Autowired
    private InspectionVisitMapper inspectionVisitMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 统计巡视方案下被巡视单位InspectionVisit次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionVisitCount(String planId) throws BaseException {
        return inspectionVisitMapper.selectInspectionVisitCount(planId);
    }

    /**
     * 查询来访列表
     *
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    public List<InspectionVisitPO> selectInspectionVisitList(InspectionVisitPO inspectionVisit) throws BaseException {
        QueryWrapper<InspectionVisitPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionVisit.getUnitsId());
        queryWrapper.eq("plan_id", inspectionVisit.getPlanId());
        return inspectionVisitMapper.selectList(queryWrapper);
    }

    /**
     * 新增来访
     *
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    public int save(InspectionVisitPO inspectionVisit) throws BaseException {
        int result = inspectionVisitMapper.insert(inspectionVisit);
        List<PiisDocumentPO> documents = inspectionVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                documentService.updateDocumentById(document.setObjectId("Visit" + inspectionVisit.getVisitId()));
            }
        }
        return result;
    }

    /**
     * 根据ID修改来访
     *
     * @param inspectionVisit
     * @return
     * @throws BaseException
     */
    public int update(InspectionVisitPO inspectionVisit) throws BaseException {
        List<PiisDocumentPO> documents = inspectionVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setObjectId("Visit" + inspectionVisit.getVisitId());
                            documentService.updateDocumentById(document);
                            break;
                        }
                        case DELETE: {
                            //删除服务器上文件以及文件表数据
                            documentService.deleteDocumentById(document.getPiisDocId());
                            String filePath = document.getFilePath();
                            if (!StringUtils.isEmpty(filePath)) {
                                FileUploadUtils.deleteServerFile(filePath.replace(serverAddr + "/upload", baseFileUrl));
                            }
                            break;
                        }
                    }
                }
            }
        }
        return inspectionVisitMapper.updateById(inspectionVisit);
    }

    /**
     * 根据ID批量删除来访
     *
     * @param callVisitIds 来访编号
     * @return
     */
    public int deleteByInspectionVisitIds(String[] callVisitIds) {
        List<String> list = Arrays.asList(callVisitIds);
        return inspectionVisitMapper.deleteBatchIds(list);
    }
}
