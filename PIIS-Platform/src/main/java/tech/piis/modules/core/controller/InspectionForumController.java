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
import tech.piis.modules.core.domain.po.InspectionForumPO;
import tech.piis.modules.core.service.IInspectionForumService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 座谈会 Controller
 *
 * @author Tuzki
 * @date 2020-12-07
 */
@RestController
@RequestMapping("/piis/forum")
public class InspectionForumController extends BaseController {
    @Autowired
    private IInspectionForumService inspectionForumService;


    /**
     * 查询座谈会 列表
     *
     * @param inspectionForum
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionForumPO inspectionForum) throws BaseException {
        startPage();
        List<InspectionForumPO> data = inspectionForumService.selectInspectionForumList(inspectionForum);
        forumCovert2List(data);
        return getDataTable(data);
    }

    /**
     * 查询座谈会 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionForumList(String planId) throws BaseException {
        return AjaxResult.success(inspectionForumService.selectInspectionForumCount(planId));
    }

    /**
     * 新增座谈会
     *
     * @param inspectionForum
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "座谈会", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionForumPO inspectionForum) {
        if (null == inspectionForum) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionForum.getIsApproval()) {
            inspectionForum.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionForum.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        forumCovert2String(inspectionForum);
        BizUtils.setCreatedOperation(InspectionForumPO.class, inspectionForum);
        return toAjax(inspectionForumService.save(inspectionForum));
    }

    /**
     * 审批座谈会
     *
     * @param inspectionForumList
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "座谈会 ", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult add(@RequestBody List<InspectionForumPO> inspectionForumList) {
        if (CollectionUtils.isEmpty(inspectionForumList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionForumService.doApprovals(inspectionForumList);
        return AjaxResult.success();
    }

    /**
     * 修改座谈会
     *
     * @param inspectionForum
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "座谈会 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionForumPO inspectionForum) throws BaseException {
        if (null == inspectionForum) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionForum.getIsApproval()) {
            inspectionForum.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionForum.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        forumCovert2String(inspectionForum);
        BizUtils.setUpdatedOperation(InspectionForumPO.class, inspectionForum);
        return toAjax(inspectionForumService.update(inspectionForum));
    }

    /**
     * 删除座谈会
     * forumIds 座谈会 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:sceneUnderstand:perms')")
    @Log(title = "座谈会 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{forumIds}")
    public AjaxResult remove(@PathVariable Long[] forumIds) throws BaseException {
        return toAjax(inspectionForumService.deleteByInspectionForumIds(forumIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionForum
     */
    private void forumCovert2String(InspectionForumPO inspectionForum) {
        if (null != inspectionForum) {
            inspectionForum.setReporterId(paramsCovert2String(inspectionForum.getReporter()).get(0));
            inspectionForum.setReporterName(paramsCovert2String(inspectionForum.getReporter()).get(1));
            inspectionForum.setInspectionGroupPersonId(paramsCovert2String(inspectionForum.getInspectionGroupPersons()).get(0));
            inspectionForum.setInspectionGroupPersonName(paramsCovert2String(inspectionForum.getInspectionGroupPersons()).get(1));
            inspectionForum.setReportPersonId(paramsCovert2String(inspectionForum.getParticipants()).get(0));
            inspectionForum.setReportPersonName(paramsCovert2String(inspectionForum.getParticipants()).get(1));
        }
    }

    /**
     * 参数类型转换
     *
     * @param inspectionForumList
     */
    private void forumCovert2List(List<InspectionForumPO> inspectionForumList) {
        if (!CollectionUtils.isEmpty(inspectionForumList)) {
            inspectionForumList.forEach(inspectionForumPO -> {
                inspectionForumPO.setReporter(paramsCovert2List(inspectionForumPO.getReporterId(), inspectionForumPO.getReporterName()));
                inspectionForumPO.setInspectionGroupPersons(paramsCovert2List(inspectionForumPO.getInspectionGroupPersonId(), inspectionForumPO.getInspectionGroupPersonName()));
                inspectionForumPO.setParticipants(paramsCovert2List(inspectionForumPO.getReportPersonId(), inspectionForumPO.getReportPersonName()));
            });
        }
    }
}
