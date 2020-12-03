package tech.piis.modules.core.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionIssuanceNoticePO;
import tech.piis.modules.core.domain.po.InspectionReportApprovalPO;
import tech.piis.modules.core.domain.vo.PatrolBriefVO;
import tech.piis.modules.core.service.IInspectionIssuanceNoticeService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;


/**
 * 印发通知 Controller
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/piis/notice")
public class InspectionIssuanceNoticeController extends BaseController
{
    @Autowired
    private IInspectionIssuanceNoticeService inspectionIssuanceNoticeService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询印发通知
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:notice:query')")
    @GetMapping("/info")
    public AjaxResult findInspectionIssuanceNotice(String planId) throws BaseException {
        InspectionIssuanceNoticePO inspectionIssuanceNoticePO = new InspectionIssuanceNoticePO()
                .setPlanId(planId);
        List<InspectionIssuanceNoticePO> inspectionIssuanceNoticeList = inspectionIssuanceNoticeService.selectInspectionIssuanceNoticeList(inspectionIssuanceNoticePO);
        AjaxResult ajaxResult = AjaxResult.success();
        if (!CollectionUtils.isEmpty(inspectionIssuanceNoticeList)) {
            PatrolBriefVO patrolBriefVO = new PatrolBriefVO()
                    .setObjectId(inspectionIssuanceNoticeList.get(0).getIssuanceNoticeId())
                    .setDocuments(documentService.getFileListByBizId("InspectionIssuanceNotice" + inspectionIssuanceNoticeList.get(0).getIssuanceNoticeId()));
            List<PatrolBriefVO> patrolBriefVOList = new ArrayList<>();
            patrolBriefVOList.add(patrolBriefVO);
            return AjaxResult.success(patrolBriefVOList);
        }
        return ajaxResult;
    }

    /**
     * 新增印发通知 
     * @param  inspectionIssuanceNotice
     */
    @PreAuthorize("@ss.hasPermi('piis:notice:add')")
    @Log(title = "印发通知 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionIssuanceNoticePO inspectionIssuanceNotice) {
        if (null == inspectionIssuanceNotice) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionIssuanceNoticePO.class, inspectionIssuanceNotice);
        return toAjax(inspectionIssuanceNoticeService.save(inspectionIssuanceNotice));
    }

    /**
     * 修改印发通知 
     * @param  inspectionIssuanceNotice
     */
    @PreAuthorize("@ss.hasPermi('piis:notice:edit')")
    @Log(title = "印发通知 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionIssuanceNoticePO inspectionIssuanceNotice) throws BaseException{
        if (null == inspectionIssuanceNotice) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionIssuanceNoticePO.class, inspectionIssuanceNotice);
        return toAjax(inspectionIssuanceNoticeService.update(inspectionIssuanceNotice));
    }
}
