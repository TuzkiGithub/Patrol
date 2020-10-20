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
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionOrganizationMeetingsPO;
import tech.piis.modules.core.service.IInspectionOrganizationMeetingsService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;


/**
 * 组织会议Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/meetings")
public class InspectionOrganizationMeetingsController extends BaseController {
    @Autowired
    private IInspectionOrganizationMeetingsService inspectionOrganizationMeetingsService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询组织会议列表
     *
     * @param inspectionOrganizationMeetings
     */
    @PreAuthorize("@ss.hasPermi('piis:meetings:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        startPage();
        List<InspectionOrganizationMeetingsPO> data = inspectionOrganizationMeetingsService.selectInspectionOrganizationMeetingsList(inspectionOrganizationMeetings);
        organizationMeetingsCovert2List(data);
        return getDataTable(data);
    }

    /**
     * 查询组织会议文件
     *
     * @param organizationMeetingsId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:meetings:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionOrganizationMeetingsFile(String organizationMeetingsId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("OrganizationMeetings" + organizationMeetingsId));
    }

    /**
     * 查询组织会议总览列表
     *
     * @param planId 巡视计划ID
     * @param organizationType 组织类型
     */
    @PreAuthorize("@ss.hasPermi('piis:meetings:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionOrganizationMeetingsList(@RequestParam("planId") String planId, @RequestParam("organizationType") Integer organizationType) throws BaseException {
        return AjaxResult.success(inspectionOrganizationMeetingsService.selectInspectionOrganizationMeetingsCount(planId, organizationType));
    }

    /**
     * 新增组织会议
     *
     * @param inspectionOrganizationMeetings
     */
    @PreAuthorize("@ss.hasPermi('piis:meetings:add')")
    @Log(title = "组织会议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) {
        if (null == inspectionOrganizationMeetings) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionOrganizationMeetingsPO.class, inspectionOrganizationMeetings);
        organizationMeetingsCovert2String(inspectionOrganizationMeetings);
        return toAjax(inspectionOrganizationMeetingsService.save(inspectionOrganizationMeetings));
    }

    /**
     * 修改组织会议
     *
     * @param inspectionOrganizationMeetings
     */
    @PreAuthorize("@ss.hasPermi('piis:meetings:edit')")
    @Log(title = "组织会议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) throws BaseException {
        if (null == inspectionOrganizationMeetings) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionOrganizationMeetingsPO.class, inspectionOrganizationMeetings);
        organizationMeetingsCovert2String(inspectionOrganizationMeetings);
        return toAjax(inspectionOrganizationMeetingsService.update(inspectionOrganizationMeetings));
    }

    /**
     * 删除组织会议
     * organizationMeetingsIds 组织会议ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:meetings:remove')")
    @Log(title = "组织会议", businessType = BusinessType.DELETE)
    @DeleteMapping("/{organizationMeetingsIds}")
    public AjaxResult remove(@PathVariable Long[] organizationMeetingsIds) throws BaseException {
        return toAjax(inspectionOrganizationMeetingsService.deleteByInspectionOrganizationMeetingsIds(organizationMeetingsIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionOrganizationMeetings
     */
    private void organizationMeetingsCovert2String(InspectionOrganizationMeetingsPO inspectionOrganizationMeetings) {
        if (null != inspectionOrganizationMeetings) {
            inspectionOrganizationMeetings.setParticipantsId(paramsCovert2String(inspectionOrganizationMeetings.getParticipants()).get(0));
            inspectionOrganizationMeetings.setParticipantsName(paramsCovert2String(inspectionOrganizationMeetings.getParticipants()).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionOrganizationMeetingsList
     */
    private void organizationMeetingsCovert2List(List<InspectionOrganizationMeetingsPO> inspectionOrganizationMeetingsList) {
        if (!CollectionUtils.isEmpty(inspectionOrganizationMeetingsList)) {
            inspectionOrganizationMeetingsList.forEach(var -> var.setParticipants(paramsCovert2List(var.getParticipantsId(), var.getParticipantsName())));
        }
    }
}