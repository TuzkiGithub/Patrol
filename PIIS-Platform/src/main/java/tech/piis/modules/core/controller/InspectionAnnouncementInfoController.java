package tech.piis.modules.core.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionAnnouncementInfoPO;
import tech.piis.modules.core.service.IInspectionAnnouncementInfoService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;
import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;
import tech.piis.common.enums.ApprovalEnum;

import javax.validation.Valid;


/**
 * 公告信息 Controller
 *
 * @author Tuzki
 * @date 2020-12-03
 */
@RestController
@RequestMapping("/piis/info")
public class InspectionAnnouncementInfoController extends BaseController {
    @Autowired
    private IInspectionAnnouncementInfoService inspectionAnnouncementInfoService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询公告信息 列表
     * @param  inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('core:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException{
        startPage();
        List<InspectionAnnouncementInfoPO> data = inspectionAnnouncementInfoService.selectInspectionAnnouncementInfoList(inspectionAnnouncementInfo);
        return getDataTable(data);
    }

    /**
     * 查询公告信息 文件
     * @param  inspectionAnnouncementInfoId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('core:info:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionAnnouncementInfoFile(@RequestParam("") String inspectionAnnouncementInfoId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("InspectionAnnouncementInfo" + inspectionAnnouncementInfoId));
    }

    /**
     * 查询公告信息 总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('core:info:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionAnnouncementInfoList(String planId) throws BaseException{
        return AjaxResult.success(inspectionAnnouncementInfoService.selectInspectionAnnouncementInfoCount(planId));
    }
    /**
     * 新增公告信息 
     * @param  inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('core:info:add')")
    @Log(title = "公告信息 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionAnnouncementInfoPO inspectionAnnouncementInfo) {
        if (null == inspectionAnnouncementInfo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionAnnouncementInfo.getIsApproval()) {
            inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setCreatedOperation(InspectionAnnouncementInfoPO.class, inspectionAnnouncementInfo);
        return toAjax(inspectionAnnouncementInfoService.save(inspectionAnnouncementInfo));
    }

    /**
     * 审批公告信息 
     * @param  inspectionAnnouncementInfoList
     */
    @PreAuthorize("@ss.hasPermi('core:info:approval')")
    @Log(title = "公告信息 ", businessType = BusinessType.INSERT)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionAnnouncementInfoPO> inspectionAnnouncementInfoList) {
        if (CollectionUtils.isEmpty(inspectionAnnouncementInfoList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionAnnouncementInfoService.doApprovals(inspectionAnnouncementInfoList);
        return AjaxResult.success();
    }

    /**
     * 修改公告信息 
     * @param  inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('core:info:edit')")
    @Log(title = "公告信息 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException{
        if (null == inspectionAnnouncementInfo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionAnnouncementInfo.getIsApproval()) {
            inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        BizUtils.setUpdatedOperation(InspectionAnnouncementInfoPO.class, inspectionAnnouncementInfo);
        return toAjax(inspectionAnnouncementInfoService.update(inspectionAnnouncementInfo));
    }

    /**
     * 删除公告信息 
     * announcementInfoIds 公告信息 ID数组
     */
    @PreAuthorize("@ss.hasPermi('core:info:remove')")
    @Log(title = "公告信息 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{announcementInfoIds}")
    public AjaxResult remove(@PathVariable Long[] announcementInfoIds) throws BaseException{
        return toAjax(inspectionAnnouncementInfoService.deleteByInspectionAnnouncementInfoIds(announcementInfoIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionAnnouncementInfo
    */
    private void inspectionAnnouncementInfoCovert2String(InspectionAnnouncementInfoPO inspectionAnnouncementInfo){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionAnnouncementInfoList
    */
    private void inspectionAnnouncementInfoCovert2List(List<InspectionAnnouncementInfoPO> inspectionAnnouncementInfoList){

    }
}
