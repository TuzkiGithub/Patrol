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
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionLearnTrainPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionLearnTrainService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.List;


/**
 * 学习培训 Controller
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/piis/train")
public class InspectionLearnTrainController extends BaseController {
    @Autowired
    private IInspectionLearnTrainService inspectionLearnTrainService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 与前端定义的文件类型
     */
    private static final Long TEMP_FILE1 = 1L;
    private static final Long TEMP_FILE2 = 2L;
    private static final Long TEMP_FILE3 = 3L;
    private static final Long TEMP_FILE4 = 4L;
    private static final Long TEMP_FILE5 = 5L;
    private static final Long TEMP_FILE6 = 6L;
    private static final Long TEMP_FILE7 = 7L;
    private static final Long TEMP_FILE8 = 8L;
    private static final Long TEMP_FILE9 = 9L;
    private static final Long TEMP_FILE10 = 10L;

    /**
     * 查询学习培训 列表
     *
     * @param inspectionLearnTrain
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionLearnTrainPO inspectionLearnTrain) throws BaseException {
        startPage();
        List<InspectionLearnTrainPO> data = inspectionLearnTrainService.selectInspectionLearnTrainList(inspectionLearnTrain);
        return getDataTable(data);
    }

    /**
     * 查询学习培训 文件
     *
     * @param inspectionLearnTrainId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @GetMapping("/file")
    public AjaxResult findInspectionLearnTrainFile(@RequestParam("learnTrainId") String inspectionLearnTrainId) throws BaseException {
        List<PiisDocumentPO> documents = documentService.getFileListByBizId("LearnTrain" + inspectionLearnTrainId);
        convertTempDict(documents);
        return AjaxResult.success(documents);
    }

    /**
     * 新增学习培训
     *
     * @param inspectionLearnTrain
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "学习培训 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionLearnTrainPO inspectionLearnTrain) {
        if (null == inspectionLearnTrain) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionLearnTrain);
        BizUtils.setCreatedOperation(InspectionLearnTrainPO.class, inspectionLearnTrain);
        return toAjax(inspectionLearnTrainService.save(inspectionLearnTrain));
    }

    /**
     * 修改学习培训
     *
     * @param inspectionLearnTrain
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "学习培训 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionLearnTrainPO inspectionLearnTrain) throws BaseException {
        if (null == inspectionLearnTrain) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionLearnTrain);
        BizUtils.setUpdatedOperation(InspectionLearnTrainPO.class, inspectionLearnTrain);
        return toAjax(inspectionLearnTrainService.update(inspectionLearnTrain));
    }

    /**
     * 删除学习培训
     * learnTrainIds 学习培训 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "学习培训 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{learnTrainIds}")
    public AjaxResult remove(@PathVariable Long[] learnTrainIds) throws BaseException {
        return toAjax(inspectionLearnTrainService.deleteByInspectionLearnTrainIds(learnTrainIds));
    }

    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param learnTrain
     */
    private void convertFileDict(InspectionLearnTrainPO learnTrain) {
        List<PiisDocumentPO> documents = learnTrain.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_FILE1.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.LEARN_TRAIN_FILE.getCode());
                } else if (TEMP_FILE2.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.GENERAL_SECRETARY_FILE.getCode());
                } else if (TEMP_FILE3.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.AUTH_APPOINT_FILE.getCode());
                } else if (TEMP_FILE4.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.MOBILIZATION_DEPLOYMENT_FILE.getCode());
                } else if (TEMP_FILE5.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PIIS_PROJECT_FILE.getCode());
                } else if (TEMP_FILE6.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PIIS_SITE_ARRANGEMENT_FILE.getCode());
                } else if (TEMP_FILE7.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PIIS_WORK_RULE_FILE.getCode());
                } else if (TEMP_FILE8.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PIIS_NEGATIVE_TALK_FILE.getCode());
                } else if (TEMP_FILE9.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.PIIS_TEMP_BRANCH_FILE.getCode());
                } else if (TEMP_FILE10.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.OPERATION_OTHER_FILE.getCode());
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
                if (FileEnum.LEARN_TRAIN_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE1);
                } else if (FileEnum.GENERAL_SECRETARY_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE2);
                } else if (FileEnum.AUTH_APPOINT_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE3);
                } else if (FileEnum.MOBILIZATION_DEPLOYMENT_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE4);
                } else if (FileEnum.PIIS_PROJECT_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE5);
                } else if (FileEnum.PIIS_SITE_ARRANGEMENT_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE6);
                } else if (FileEnum.PIIS_WORK_RULE_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE7);
                } else if (FileEnum.PIIS_NEGATIVE_TALK_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE8);
                } else if (FileEnum.PIIS_TEMP_BRANCH_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE9);
                } else if (FileEnum.OPERATION_OTHER_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_FILE10);
                }
            });
        }
    }
}
