package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionPresentMeetingPO;
import tech.piis.modules.core.service.IInspectionPresentMeetingService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 列席会议 Controller
 *
 * @author Tuzki
 * @date 2020-12-07
 */
@RestController
@RequestMapping("/piis/present/meeting")
public class InspectionPresentMeetingController extends BaseController {
    @Autowired
    private IInspectionPresentMeetingService inspectionPresentMeetingService;


    /**
     * 查询列席会议 列表
     *
     * @param inspectionPresentMeeting
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException {
        startPage();
        List<InspectionPresentMeetingPO> data = inspectionPresentMeetingService.selectInspectionPresentMeetingList(inspectionPresentMeeting);
        inspectionPresentMeetingCovert2List(data);
        return getDataTable(data);
    }

    /**
     * 查询列席会议 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionPresentMeetingList(String planId) throws BaseException {
        return AjaxResult.success(inspectionPresentMeetingService.selectInspectionPresentMeetingCount(planId));
    }

    /**
     * 新增列席会议
     *
     * @param inspectionPresentMeeting
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "列席会议 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionPresentMeetingPO inspectionPresentMeeting) {
        if (null == inspectionPresentMeeting) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionPresentMeeting.getIsApproval()) {
            inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        inspectionPresentMeetingCovert2String(inspectionPresentMeeting);
        BizUtils.setCreatedOperation(InspectionPresentMeetingPO.class, inspectionPresentMeeting);
        return toAjax(inspectionPresentMeetingService.save(inspectionPresentMeeting));
    }

    /**
     * 审批列席会议
     *
     * @param inspectionPresentMeetingList
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "列席会议 ", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionPresentMeetingPO> inspectionPresentMeetingList) {
        if (CollectionUtils.isEmpty(inspectionPresentMeetingList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionPresentMeetingService.doApprovals(inspectionPresentMeetingList);
        return AjaxResult.success();
    }

    /**
     * 修改列席会议
     *
     * @param inspectionPresentMeeting
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "列席会议 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionPresentMeetingPO inspectionPresentMeeting) throws BaseException {
        if (null == inspectionPresentMeeting) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionPresentMeeting.getIsApproval()) {
            inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionPresentMeeting.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        inspectionPresentMeetingCovert2String(inspectionPresentMeeting);
        BizUtils.setUpdatedOperation(InspectionPresentMeetingPO.class, inspectionPresentMeeting);
        return toAjax(inspectionPresentMeetingService.update(inspectionPresentMeeting));
    }

    /**
     * 删除列席会议
     * presentMeetingIds 列席会议 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "列席会议 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{presentMeetingIds}")
    public AjaxResult remove(@PathVariable Long[] presentMeetingIds) throws BaseException {
        return toAjax(inspectionPresentMeetingService.deleteByInspectionPresentMeetingIds(presentMeetingIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionPresentMeeting
     */
    private void inspectionPresentMeetingCovert2String(InspectionPresentMeetingPO inspectionPresentMeeting) {
        if (null != inspectionPresentMeeting) {
            inspectionPresentMeeting.setReporterId(paramsCovert2String(inspectionPresentMeeting.getReporter()).get(0));
            inspectionPresentMeeting.setReporterName(paramsCovert2String(inspectionPresentMeeting.getReporter()).get(1));
            inspectionPresentMeeting.setInspectionGroupPersonId(paramsCovert2String(inspectionPresentMeeting.getInspectionGroupPersons()).get(0));
            inspectionPresentMeeting.setInspectionGroupPersonName(paramsCovert2String(inspectionPresentMeeting.getInspectionGroupPersons()).get(1));
            inspectionPresentMeeting.setReportPersonId(paramsCovert2String(inspectionPresentMeeting.getParticipants()).get(0));
            inspectionPresentMeeting.setReportPersonName(paramsCovert2String(inspectionPresentMeeting.getParticipants()).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionPresentMeetingList
     */
    private void inspectionPresentMeetingCovert2List(List<InspectionPresentMeetingPO> inspectionPresentMeetingList) {
        if (!CollectionUtils.isEmpty(inspectionPresentMeetingList)) {
            inspectionPresentMeetingList.forEach(inspectionPresentMeetingPO -> {
                inspectionPresentMeetingPO.setReporter(paramsCovert2List(inspectionPresentMeetingPO.getReporterId(), inspectionPresentMeetingPO.getReporterName()));
                inspectionPresentMeetingPO.setInspectionGroupPersons(paramsCovert2List(inspectionPresentMeetingPO.getInspectionGroupPersonId(), inspectionPresentMeetingPO.getInspectionGroupPersonName()));
                inspectionPresentMeetingPO.setParticipants(paramsCovert2List(inspectionPresentMeetingPO.getReportPersonId(), inspectionPresentMeetingPO.getReportPersonName()));
            });
        }
    }
}
