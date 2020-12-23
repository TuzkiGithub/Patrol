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
import tech.piis.modules.core.domain.po.InspectionReportApprovalPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.domain.vo.PatrolBriefVO;
import tech.piis.modules.core.domain.vo.UnitsBriefVO;
import tech.piis.modules.core.domain.vo.UserBriefVO;
import tech.piis.modules.core.service.IInspectionReportApprovalService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.ArrayList;
import java.util.List;


/**
 * 报请审批 Controller
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/piis/approval")
public class InspectionReportApprovalController extends BaseController {
    @Autowired
    private IInspectionReportApprovalService inspectionReportApprovalService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 与前端定义的文件
     */
    private static final Long TEMP_OTHER_FILE_ONE = 1L;
    private static final Long TEMP_OTHER_FILE_TWO = 2L;

    /**
     * 查询报请审批
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @GetMapping("/info")
    public AjaxResult findInspectionReportApprovalFile(String planId) throws BaseException {
        InspectionReportApprovalPO reportApproval = new InspectionReportApprovalPO()
                .setPlanId(planId);
        List<InspectionReportApprovalPO> reportApprovalList = inspectionReportApprovalService.selectInspectionReportApprovalList(reportApproval);
        reportApprovalCovert2List(reportApprovalList);
        AjaxResult ajaxResult = AjaxResult.success();
        if (!CollectionUtils.isEmpty(reportApprovalList)) {
            InspectionReportApprovalPO result = reportApprovalList.get(0);
            List<PiisDocumentPO> documents = documentService.getFileListByBizId("ReportApproval" + result.getReportApprovalId());
            convertTempDict(documents);
            result.setDocuments(documents);
            return AjaxResult.success(reportApprovalList);
        }
        return ajaxResult;
    }

    /**
     * 新增报请审批
     *
     * @param inspectionReportApproval
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "报请审批 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionReportApprovalPO inspectionReportApproval) {
        if (null == inspectionReportApproval) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        reportApprovalCovert2String(inspectionReportApproval);
        convertFileDict(inspectionReportApproval);
        BizUtils.setCreatedOperation(InspectionReportApprovalPO.class, inspectionReportApproval);
        return toAjax(inspectionReportApprovalService.save(inspectionReportApproval));
    }

    /**
     * 修改报请审批
     *
     * @param inspectionReportApproval
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "报请审批 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionReportApprovalPO inspectionReportApproval) throws BaseException {
        if (null == inspectionReportApproval) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        reportApprovalCovert2String(inspectionReportApproval);
        convertFileDict(inspectionReportApproval);
        BizUtils.setUpdatedOperation(InspectionReportApprovalPO.class, inspectionReportApproval);
        return toAjax(inspectionReportApprovalService.update(inspectionReportApproval));
    }

    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionReportApproval
     */
    private void convertFileDict(InspectionReportApprovalPO inspectionReportApproval) {
        List<PiisDocumentPO> documents = inspectionReportApproval.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_OTHER_FILE_ONE.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.REPORT_OTHER_FILE_ONE.getCode());
                } else if (TEMP_OTHER_FILE_TWO.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.REPORT_OTHER_FILE_TWO.getCode());
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
                if (FileEnum.REPORT_OTHER_FILE_ONE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_OTHER_FILE_ONE);
                } else if (FileEnum.REPORT_OTHER_FILE_TWO.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_OTHER_FILE_TWO);
                }
            });
        }
    }

    /**
     * 参数类型转换
     *
     * @param inspectionReportApproval
     */
    private void reportApprovalCovert2String(InspectionReportApprovalPO inspectionReportApproval) {
        if (null != inspectionReportApproval) {
            List<UserBriefVO> forwardSendList = inspectionReportApproval.getForwardSendList();
            List<UnitsBriefVO> orgList = inspectionReportApproval.getSignOrgList();
            inspectionReportApproval.setForwardSendIds(BizUtils.paramsCovert2String(forwardSendList).get(0));
            inspectionReportApproval.setForwardSendNames(BizUtils.paramsCovert2String(forwardSendList).get(1));
            inspectionReportApproval.setSignDeptIds(BizUtils.paramsCovert2OrgString(orgList).get(0));
            inspectionReportApproval.setSignDeptNames(BizUtils.paramsCovert2OrgString(orgList).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionReportApprovalList
     */
    private void reportApprovalCovert2List(List<InspectionReportApprovalPO> inspectionReportApprovalList) {
        if (!CollectionUtils.isEmpty(inspectionReportApprovalList)) {
            inspectionReportApprovalList.forEach(var -> {
                var.setForwardSendList(BizUtils.paramsCovert2List(var.getForwardSendIds(), var.getForwardSendNames()));
                var.setSignOrgList(BizUtils.paramsCovert2OrgList(var.getSignDeptIds(), var.getSignDeptNames()));
            });
        }

    }
}
