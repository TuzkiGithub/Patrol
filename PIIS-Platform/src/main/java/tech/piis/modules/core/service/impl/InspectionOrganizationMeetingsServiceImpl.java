package tech.piis.modules.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.modules.core.domain.po.InspectionOrganizationMeetingsPO;
import tech.piis.modules.core.domain.po.InspectionUnitsPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.UnitsBizCountVO;
import tech.piis.modules.core.mapper.InspectionOrganizationMeetingsMapper;
import tech.piis.modules.core.mapper.InspectionUnitsMapper;
import tech.piis.modules.core.service.IInspectionOrganizationMeetingsService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.Arrays;
import java.util.List;

import static tech.piis.common.constant.OperationConstants.DELETE;
import static tech.piis.common.constant.OperationConstants.INSERT;

/**
 * 组织会议Service业务层处理
 *
 * @author Kevin
 * @date 2020-10-19
 */
@Transactional
@Service
public class InspectionOrganizationMeetingsServiceImpl implements IInspectionOrganizationMeetingsService {
    @Autowired
    private InspectionOrganizationMeetingsMapper inspectionOrganizationMeetingsMapper;

    @Autowired
    private InspectionUnitsMapper unitsMapper;

    @Autowired
    private IPiisDocumentService documentService;

    @Value("${piis.profile}")
    private String baseFileUrl;

    @Value("${piis.serverAddr}")
    private String serverAddr;

    /**
     * 组织类型
     */
    private static final Integer TEMP_ORG_OTHER_FILE = 0;
    private static final Integer TEMP_BRANCH_OTHER_FILE = 1;

    /**
     * 统计巡视方案下被巡视单位InspectionOrganizationMeetings次数
     *
     * @param planId           巡视计划ID
     * @param organizationType 组织类型
     */
    public List<UnitsBizCountVO> selectInspectionOrganizationMeetingsCount(String planId, Integer organizationType) throws BaseException {
        List<UnitsBizCountVO> unitsBizCountVOS = inspectionOrganizationMeetingsMapper.selectInspectionOrganizationMeetingsCount(planId, organizationType);
        if (!CollectionUtils.isEmpty(unitsBizCountVOS)) {
            unitsBizCountVOS.forEach(var -> {
                Integer currentOrganizationType = var.getOrganizationType();
                if (!organizationType.equals(currentOrganizationType)) {
                    var.setCount(0);
                }
            });
        }


        return unitsBizCountVOS;
    }

    /**
     * 查询组织会议列表
     *
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    public List<InspectionOrganizationMeetingsPO> selectInspectionOrganizationMeetingsList(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        QueryWrapper<InspectionOrganizationMeetingsPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("units_id", inspectionOrganizationMeetings.getUnitsId());
        queryWrapper.eq("plan_id", inspectionOrganizationMeetings.getPlanId());
        queryWrapper.eq("ORGANIZATION_TYPE", inspectionOrganizationMeetings.getOrganizationType());
        return inspectionOrganizationMeetingsMapper.selectList(queryWrapper);
    }

    /**
     * 新增组织会议
     *
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    public int save(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        int result = inspectionOrganizationMeetingsMapper.insert(inspectionOrganizationMeetings);
        List<PiisDocumentPO> documents = inspectionOrganizationMeetings.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                BizUtils.setUpdatedOperation(PiisDocumentPO.class, document);
                Long dictId = getFileDictId(inspectionOrganizationMeetings.getOrganizationType());
                documentService.updateDocumentById(document.setObjectId("OrganizationMeetings" + inspectionOrganizationMeetings.getOrganizationMeetingsId()).setFileDictId(dictId));
            }
        }
        return result;
    }

    /**
     * 根据ID修改组织会议
     *
     * @param inspectionOrganizationMeetings
     * @return
     * @throws BaseException
     */
    public int update(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        List<PiisDocumentPO> documents = inspectionOrganizationMeetings.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            for (PiisDocumentPO document : documents) {
                Integer operationType = document.getOperationType();
                if (null != operationType) {
                    switch (operationType) {
                        case INSERT: {
                            //将业务字段赋予文件表
                            Integer organizationType = inspectionOrganizationMeetings.getOrganizationType();
                            Long dictId = getFileDictId(organizationType);
                            document.setFileDictId(dictId)
                                    .setObjectId("OrganizationMeetings" + inspectionOrganizationMeetings.getOrganizationMeetingsId());
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
        return inspectionOrganizationMeetingsMapper.updateById(inspectionOrganizationMeetings);
    }

    /**
     * 根据ID批量删除组织会议
     *
     * @param organizationMeetingsIds 组织会议编号
     * @return
     */
    public int deleteByInspectionOrganizationMeetingsIds(Long[] organizationMeetingsIds) {
        List<Long> list = Arrays.asList(organizationMeetingsIds);
        return inspectionOrganizationMeetingsMapper.deleteBatchIds(list);
    }

    /**
     * 获取文件类型ID
     *
     * @param orgType
     * @return
     */
    private Long getFileDictId(Integer orgType) {
        Long dictId = null;
        if (TEMP_ORG_OTHER_FILE.equals(orgType)) {
            dictId = FileEnum.ORG_OTHER_FILE.getCode();
        } else if (TEMP_BRANCH_OTHER_FILE.equals(orgType)) {
            dictId = FileEnum.BRANCH_OTHER_FILE.getCode();
        }
        return dictId;
    }


    /**
     * 判断集合是否包含当前对象
     *
     * @param units
     * @param unitsBizCountVOS
     * @return
     */
    private boolean isContainUnits(InspectionUnitsPO units, List<UnitsBizCountVO> unitsBizCountVOS) {
        if (!CollectionUtils.isEmpty(unitsBizCountVOS)) {
            for (UnitsBizCountVO unitsBizCountVO : unitsBizCountVOS) {
                if (unitsBizCountVO.getUnitsId().equals(units.getUnitsId())) {
                    return true;
                }
            }
        }
        return false;
    }
}