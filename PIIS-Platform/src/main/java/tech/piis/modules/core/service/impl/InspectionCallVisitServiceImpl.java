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
import tech.piis.modules.core.domain.po.InspectionCallVisitPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionCallVisitMapper;
import tech.piis.modules.core.service.IInspectionCallVisitService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 来电Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionCallVisitServiceImpl implements IInspectionCallVisitService {
    @Autowired
    private InspectionCallVisitMapper inspectionCallVisitMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 统计巡视方案下被巡视单位InspectionCallVisit次数
     *
     * @param planId 巡视计划ID
     */
    public List<UnitsBizCountVO> selectInspectionCallVisitCount(String planId) throws BaseException {
        return inspectionCallVisitMapper.selectInspectionCallVisitCount(planId);
    }

    /**
     * 查询来电列表
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    public List<InspectionCallVisitPO> selectInspectionCallVisitList(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        QueryWrapper<InspectionCallVisitPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionCallVisit.getUnitsId());
        queryWrapper.eq("plan_id", inspectionCallVisit.getPlanId());
        return inspectionCallVisitMapper.selectList(queryWrapper);
    }

    /**
     * 新增来电
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    public int save(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        int result = inspectionCallVisitMapper.insert(inspectionCallVisit);
        List<PiisDocumentPO> documents = inspectionCallVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                documentService.updateDocumentById(document.setObjectId("CallVisit" + inspectionCallVisit.getCallVisitId()));
            }
        }
        return result;
    }

    /**
     * 根据ID修改来电
     *
     * @param inspectionCallVisit
     * @return
     * @throws BaseException
     */
    public int update(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        List<PiisDocumentPO> documents = inspectionCallVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            document.setObjectId("CallVisit" + inspectionCallVisit.getCallVisitId());
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
        return inspectionCallVisitMapper.updateById(inspectionCallVisit);
    }

    /**
     * 根据ID批量删除来电
     *
     * @param callVisitIds 来电编号
     * @return
     */
    public int deleteByInspectionCallVisitIds(String[] callVisitIds) {
        List<String> list = Arrays.asList(callVisitIds);
        return inspectionCallVisitMapper.deleteBatchIds(list);
    }
}
