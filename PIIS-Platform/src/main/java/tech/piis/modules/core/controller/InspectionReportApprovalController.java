package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionReportApprovalPO;
import tech.piis.modules.core.domain.vo.PatrolBriefVO;
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
     * 查询报请审批
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:approval:query')")
    @GetMapping("/info")
    public AjaxResult findInspectionReportApprovalFile(String planId) throws BaseException {
        InspectionReportApprovalPO reportApproval = new InspectionReportApprovalPO()
                .setPlanId(planId);
        List<InspectionReportApprovalPO> reportApprovalList = inspectionReportApprovalService.selectInspectionReportApprovalList(reportApproval);
        AjaxResult ajaxResult = AjaxResult.success();
        if (!CollectionUtils.isEmpty(reportApprovalList)) {
            PatrolBriefVO patrolBriefVO = new PatrolBriefVO()
                    .setObjectId(reportApprovalList.get(0).getReportApprovalId())
                    .setDocuments(documentService.getFileListByBizId("InspectionReportApproval" + reportApprovalList.get(0).getReportApprovalId()));
            List<PatrolBriefVO> patrolBriefVOList = new ArrayList<>();
            patrolBriefVOList.add(patrolBriefVO);
            return AjaxResult.success(patrolBriefVOList);
        }
        return ajaxResult;
    }

    /**
     * 新增报请审批
     *
     * @param inspectionReportApproval
     */
    @PreAuthorize("@ss.hasPermi('piis:approval:add')")
    @Log(title = "报请审批 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionReportApprovalPO inspectionReportApproval) {
        if (null == inspectionReportApproval) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionReportApprovalPO.class, inspectionReportApproval);
        return toAjax(inspectionReportApprovalService.save(inspectionReportApproval));
    }

    /**
     * 修改报请审批
     *
     * @param inspectionReportApproval
     */
    @PreAuthorize("@ss.hasPermi('piis:approval:edit')")
    @Log(title = "报请审批 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionReportApprovalPO inspectionReportApproval) throws BaseException {
        if (null == inspectionReportApproval) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionReportApprovalPO.class, inspectionReportApproval);
        return toAjax(inspectionReportApprovalService.update(inspectionReportApproval));
    }
}
