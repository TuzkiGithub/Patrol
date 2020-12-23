package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionSpotMaterialPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PatrolBriefVO;
import tech.piis.modules.core.service.IInspectionSpotMaterialService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * 驻场材料 Controller
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/piis/material")
public class InspectionSpotMaterialController extends BaseController {
    @Autowired
    private IInspectionSpotMaterialService inspectionSpotMaterialService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 与前端定义的文件类型
     */
    private static final Long TEMP_FILE1 = 1L;
    private static final Long TEMP_FILE2 = 2L;
    private static final Long TEMP_FILE3 = 3L;
    private static final Long TEMP_FILE4 = 4L;

    /**
     * 查询驻场材料 文件
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @GetMapping("/info")
    public AjaxResult findInspectionSpotMaterialFile(String planId) throws BaseException {
        InspectionSpotMaterialPO spotMaterialPO = new InspectionSpotMaterialPO()
                .setPlanId(planId);
        List<InspectionSpotMaterialPO> spotMaterialList = inspectionSpotMaterialService.selectInspectionSpotMaterialList(spotMaterialPO);

        AjaxResult ajaxResult = AjaxResult.success();
        if (!CollectionUtils.isEmpty(spotMaterialList)) {
            List<PiisDocumentPO> documents = documentService.getFileListByBizId("SpotMaterial" + spotMaterialList.get(0).getSpotMaterialId());
            convertTempDict(documents);
            PatrolBriefVO patrolBriefVO = new PatrolBriefVO()
                    .setObjectId(spotMaterialList.get(0).getSpotMaterialId())
                    .setDocuments(documents);
            List<PatrolBriefVO> patrolBriefVOList = new ArrayList<>();
            patrolBriefVOList.add(patrolBriefVO);
            return AjaxResult.success(patrolBriefVOList);
        }
        return ajaxResult;
    }

    /**
     * 新增驻场材料
     *
     * @param inspectionSpotMaterial
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "驻场材料 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionSpotMaterialPO inspectionSpotMaterial) {
        if (null == inspectionSpotMaterial) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionSpotMaterial);
        BizUtils.setCreatedOperation(InspectionSpotMaterialPO.class, inspectionSpotMaterial);
        return toAjax(inspectionSpotMaterialService.save(inspectionSpotMaterial));
    }

    /**
     * 修改驻场材料
     *
     * @param inspectionSpotMaterial
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "驻场材料 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionSpotMaterialPO inspectionSpotMaterial) throws BaseException {
        if (null == inspectionSpotMaterial) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionSpotMaterial);
        BizUtils.setUpdatedOperation(InspectionSpotMaterialPO.class, inspectionSpotMaterial);
        return toAjax(inspectionSpotMaterialService.update(inspectionSpotMaterial));
    }


    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionSpotMaterial
     */
    private void convertFileDict(InspectionSpotMaterialPO inspectionSpotMaterial) {
        List<PiisDocumentPO> documents = inspectionSpotMaterial.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_FILE1.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.SPOT_MATERIALS_FILE.getCode());
                } else if (TEMP_FILE2.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.GROUP_SPEECH_FILE.getCode());
                } else if (TEMP_FILE3.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.SPOT_NEWS_FILE.getCode());
                } else if (TEMP_FILE4.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.SPOT_OTHER_FILE.getCode());
                }
            });
        }
    }


    /**
     * 实际文件字典 -》 临时文件字典
     *
     * @param documents
     */
    private void convertTempDict(List<PiisDocumentPO> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long DictId = document.getFileDictId();
                if (FileEnum.SPOT_MATERIALS_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE1);
                } else if (FileEnum.GROUP_SPEECH_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE2);
                } else if (FileEnum.SPOT_NEWS_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE3);
                } else if (FileEnum.SPOT_OTHER_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE4);
                }
            });
        }
    }
}
