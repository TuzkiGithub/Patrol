package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionCallVisitPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionCallVisitService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 来电Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/call/visit")
public class
InspectionCallVisitController extends BaseController {
    @Autowired
    private IInspectionCallVisitService inspectionCallVisitService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 与前端接口定义文件类型
     */
    private static final Long TEMP_CALL_OTHER_FILE = 1L;
    private static final Long TEMP_CALL_SEAL_FILE = 0L;

    /**
     * 查询来电列表
     *
     * @param inspectionCallVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        startPage();
        List<InspectionCallVisitPO> data = inspectionCallVisitService.selectInspectionCallVisitList(inspectionCallVisit);
        return getDataTable(data);
    }

    /**
     * 查询来电文件
     *
     * @param callVisitId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionCallVisitFile(String callVisitId) throws BaseException {
        List<PiisDocumentPO> documents = documentService.getFileListByBizId("CallVisit" + callVisitId);
        convertTempDict(documents);
        return AjaxResult.success(documents);
    }

    /**
     * 新增来电
     *
     * @param inspectionCallVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:add')")
    @Log(title = "来电", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionCallVisitPO inspectionCallVisit) {
        if (null == inspectionCallVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionCallVisit.getIsApproval()) {
            inspectionCallVisit.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionCallVisit.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        convertFileDict(inspectionCallVisit);
        BizUtils.setCreatedOperation(InspectionCallVisitPO.class, inspectionCallVisit);
        return toAjax(inspectionCallVisitService.save(inspectionCallVisit));
    }

    /**
     * 审批来电
     *
     * @param callVisitList
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:approval')")
    @Log(title = "来电", businessType = BusinessType.INSERT)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody List<InspectionCallVisitPO> callVisitList) {
        if (CollectionUtils.isEmpty(callVisitList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionCallVisitService.doApprovals(callVisitList);
        return AjaxResult.success();
    }

    /**
     * 修改来电
     *
     * @param inspectionCallVisit
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:edit')")
    @Log(title = "来电", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionCallVisitPO inspectionCallVisit) throws BaseException {
        if (null == inspectionCallVisit) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionCallVisit.getIsApproval()) {
            inspectionCallVisit.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionCallVisit.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        convertFileDict(inspectionCallVisit);
        BizUtils.setUpdatedOperation(InspectionCallVisitPO.class, inspectionCallVisit);
        return toAjax(inspectionCallVisitService.update(inspectionCallVisit));
    }

    /**
     * 删除来电
     * callVisitIds 来电ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:call/visit:remove')")
    @Log(title = "来电", businessType = BusinessType.DELETE)
    @DeleteMapping("/{callVisitIds}")
    public AjaxResult remove(@PathVariable String[] callVisitIds) throws BaseException {
        return toAjax(inspectionCallVisitService.deleteByInspectionCallVisitIds(callVisitIds));
    }

    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionCallVisit
     */
    private void convertFileDict(InspectionCallVisitPO inspectionCallVisit) {
        List<PiisDocumentPO> documents = inspectionCallVisit.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_CALL_OTHER_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.CALL_OTHER_FILE.getCode());
                } else if (TEMP_CALL_SEAL_FILE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.CALL_SEAL_FILE.getCode());
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
                if (FileEnum.CALL_OTHER_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_CALL_OTHER_FILE);
                } else if (FileEnum.CALL_SEAL_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_CALL_SEAL_FILE);
                }
            });
        }
    }
}
