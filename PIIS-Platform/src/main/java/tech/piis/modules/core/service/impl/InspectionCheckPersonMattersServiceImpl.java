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
import tech.piis.modules.core.mapper.InspectionCheckPersonMattersMapper;
import tech.piis.modules.core.domain.po.InspectionCheckPersonMattersPO;
import tech.piis.modules.core.service.IInspectionCheckPersonMattersService;

/**
 * 抽查个人事项报告Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionCheckPersonMattersServiceImpl implements IInspectionCheckPersonMattersService {
    @Autowired
    private InspectionCheckPersonMattersMapper inspectionCheckPersonMattersMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    /**
     * 统计巡视方案下被巡视单位InspectionCheckPersonMatters次数
     * @param planId 巡视计划ID
     *
     */
    public List<UnitsBizCountVO> selectInspectionCheckPersonMattersCount(String planId) throws BaseException {
        return inspectionCheckPersonMattersMapper.selectInspectionCheckPersonMattersCount(planId);
    }

    /**
     * 查询抽查个人事项报告列表
     * @param inspectionCheckPersonMatters
     * @return
     * @throws BaseException
     */
    public List<InspectionCheckPersonMattersPO> selectInspectionCheckPersonMattersList(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException {
        QueryWrapper<InspectionCheckPersonMattersPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionCheckPersonMatters.getUnitsId());
        queryWrapper.eq("plan_id", inspectionCheckPersonMatters.getPlanId());
        return inspectionCheckPersonMattersMapper.selectList(queryWrapper);
    }

    /**
     * 新增抽查个人事项报告
     * @param inspectionCheckPersonMatters
     * @return
     * @throws BaseException
     */
    public int save(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException {
        int result = inspectionCheckPersonMattersMapper.insert(inspectionCheckPersonMatters);
        List<PiisDocumentPO> documents = inspectionCheckPersonMatters.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                documentService.updateDocumentById(document.setObjectId(null).setFileDictId(null));
            }
        }
        return result;
    }

    /**
     * 根据ID修改抽查个人事项报告
     * @param inspectionCheckPersonMatters
     * @return
     * @throws BaseException
     */
    public int update(InspectionCheckPersonMattersPO inspectionCheckPersonMatters) throws BaseException {
        List<PiisDocumentPO> documents = inspectionCheckPersonMatters.getDocuments();
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
        return inspectionCheckPersonMattersMapper.updateById(inspectionCheckPersonMatters);
    }

    /**
     * 根据ID批量删除抽查个人事项报告
     * @param checkPersonMattersIds 抽查个人事项报告编号
     *
     * @return
     */
    public int deleteByInspectionCheckPersonMattersIds(Long[] checkPersonMattersIds) {
        List<Long> list = Arrays.asList(checkPersonMattersIds);
        return inspectionCheckPersonMattersMapper.deleteBatchIds(list);
    }
}
