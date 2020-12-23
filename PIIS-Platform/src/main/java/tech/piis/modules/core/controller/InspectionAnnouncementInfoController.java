package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.OperationEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionAnnouncementInfoPO;
import tech.piis.modules.core.service.IInspectionAnnouncementInfoService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 公告信息 Controller
 *
 * @author Tuzki
 * @date 2020-12-03
 */
@RestController
@RequestMapping("/piis/notice/info")
public class InspectionAnnouncementInfoController extends BaseController {
    @Autowired
    private IInspectionAnnouncementInfoService inspectionAnnouncementInfoService;

    /**
     * 查询公告信息列表
     *
     * @param inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException {
        startPage();
        List<InspectionAnnouncementInfoPO> data = inspectionAnnouncementInfoService.selectInspectionAnnouncementInfoList(inspectionAnnouncementInfo);
        return getDataTable(data);
    }


    /**
     * 查询公告信息 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionAnnouncementInfoList(String planId) throws BaseException {
        return AjaxResult.success(inspectionAnnouncementInfoService.selectInspectionAnnouncementInfoCount(planId));
    }

    /**
     * 新增公告信息
     *
     * @param inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "公告信息 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException {
        if (null == inspectionAnnouncementInfo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionAnnouncementInfoPO.class, inspectionAnnouncementInfo);
        return toAjax(inspectionAnnouncementInfoService.save(inspectionAnnouncementInfo));
    }

    /**
     * 修改公告信息
     *
     * @param inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "公告信息 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionAnnouncementInfoPO inspectionAnnouncementInfo) throws BaseException {
        if (null == inspectionAnnouncementInfo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionAnnouncementInfoPO.class, inspectionAnnouncementInfo);
        return toAjax(inspectionAnnouncementInfoService.update(inspectionAnnouncementInfo));
    }

    /**
     * 保存巡视动员会
     *
     * @param inspectionAnnouncementInfoPO
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.INSERT)
    @PostMapping("preservation")
    public AjaxResult preservation(@RequestBody @Valid InspectionAnnouncementInfoPO inspectionAnnouncementInfoPO) {
        inspectionAnnouncementInfoPO.setSubmitFlag(false);
        inspectionAnnouncementInfoPO.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        if (NO_APPROVAL == inspectionAnnouncementInfoPO.getIsApproval()) {
            inspectionAnnouncementInfoPO.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        }
        if (OperationEnum.INSERT.getCode() == inspectionAnnouncementInfoPO.getSaveFlag()) {
            return add(inspectionAnnouncementInfoPO);
        } else if (OperationEnum.UPDATE.getCode() == inspectionAnnouncementInfoPO.getSaveFlag()) {
            return edit(inspectionAnnouncementInfoPO);
        }
        return AjaxResult.error();
    }

    /**
     * 提交巡视动员会
     *
     * @param inspectionAnnouncementInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "巡视动员会", businessType = BusinessType.INSERT)
    @PostMapping("submit")
    public AjaxResult submit(@RequestBody @Valid InspectionAnnouncementInfoPO inspectionAnnouncementInfo) {
        inspectionAnnouncementInfo.setSubmitFlag(true);
        inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.SUBMITTING.getCode());
        if (NO_APPROVAL == inspectionAnnouncementInfo.getIsApproval()) {
            inspectionAnnouncementInfo.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        }
        if (OperationEnum.INSERT.getCode() == inspectionAnnouncementInfo.getSaveFlag()) {
            return add(inspectionAnnouncementInfo);
        } else if (OperationEnum.UPDATE.getCode() == inspectionAnnouncementInfo.getSaveFlag()) {
            return edit(inspectionAnnouncementInfo);
        }
        return AjaxResult.error();
    }


    /**
     * 删除公告信息
     * announcementInfoIds 公告信息 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "公告信息 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{announcementInfoIds}")
    public AjaxResult remove(@PathVariable Long[] announcementInfoIds) throws BaseException {
        return toAjax(inspectionAnnouncementInfoService.deleteByInspectionAnnouncementInfoIds(announcementInfoIds));
    }

}
