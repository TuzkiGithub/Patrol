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
import tech.piis.modules.core.domain.po.InspectionCommitteeMeetingsPO;
import tech.piis.modules.core.service.IInspectionCommitteeMeetingsService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.List;


/**
 * 党委会小组会纪要 Controller
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/piis/committee/meetings")
public class InspectionCommitteeMeetingsController extends BaseController {
    @Autowired
    private IInspectionCommitteeMeetingsService inspectionCommitteeMeetingsService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询党委会小组会纪要 列表
     *
     * @param inspectionCommitteeMeetings
     */
    @PreAuthorize("@ss.hasPermi('core:committee/meetings:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException {
        startPage();
        List<InspectionCommitteeMeetingsPO> data = inspectionCommitteeMeetingsService.selectInspectionCommitteeMeetingsList(inspectionCommitteeMeetings);
        inspectionCommitteeMeetingsCovert2List(data);
        return getDataTable(data);
    }

    /**
     * 查询党委会小组会纪要 文件
     *
     * @param inspectionCommitteeMeetingsId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('core:committee/meetings:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionCommitteeMeetingsFile(@RequestParam("committeeMeetingsId") String inspectionCommitteeMeetingsId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("InspectionCommitteeMeetings" + inspectionCommitteeMeetingsId));
    }

    /**
     * 查询党委会小组会纪要 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('core:committee/meetings:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionCommitteeMeetingsList(String planId) throws BaseException {
        return AjaxResult.success(inspectionCommitteeMeetingsService.selectInspectionCommitteeMeetingsCount(planId));
    }

    /**
     * 新增党委会小组会纪要
     *
     * @param inspectionCommitteeMeetings
     */
    @PreAuthorize("@ss.hasPermi('core:committee/meetings:add')")
    @Log(title = "党委会小组会纪要 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) {
        if (null == inspectionCommitteeMeetings) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionCommitteeMeetingsCovert2String(inspectionCommitteeMeetings);
        BizUtils.setCreatedOperation(InspectionCommitteeMeetingsPO.class, inspectionCommitteeMeetings);
        return toAjax(inspectionCommitteeMeetingsService.save(inspectionCommitteeMeetings));
    }

    /**
     * 修改党委会小组会纪要
     *
     * @param inspectionCommitteeMeetings
     */
    @PreAuthorize("@ss.hasPermi('core:committee/meetings:edit')")
    @Log(title = "党委会小组会纪要 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) throws BaseException {
        if (null == inspectionCommitteeMeetings) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionCommitteeMeetingsCovert2String(inspectionCommitteeMeetings);
        BizUtils.setUpdatedOperation(InspectionCommitteeMeetingsPO.class, inspectionCommitteeMeetings);
        return toAjax(inspectionCommitteeMeetingsService.update(inspectionCommitteeMeetings));
    }

    /**
     * 删除党委会小组会纪要
     * committeeMeetingsIds 党委会小组会纪要 ID数组
     */
    @PreAuthorize("@ss.hasPermi('core:committee/meetings:remove')")
    @Log(title = "党委会小组会纪要 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{committeeMeetingsIds}")
    public AjaxResult remove(@PathVariable Long[] committeeMeetingsIds) throws BaseException {
        return toAjax(inspectionCommitteeMeetingsService.deleteByInspectionCommitteeMeetingsIds(committeeMeetingsIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionCommitteeMeetings
     */
    private void inspectionCommitteeMeetingsCovert2String(InspectionCommitteeMeetingsPO inspectionCommitteeMeetings) {
        if (null != inspectionCommitteeMeetings) {
            inspectionCommitteeMeetings.setParticipantsId(paramsCovert2String(inspectionCommitteeMeetings.getParticipantList()).get(0));
            inspectionCommitteeMeetings.setParticipantsName(paramsCovert2String(inspectionCommitteeMeetings.getParticipantList()).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionCommitteeMeetingsList
     */
    private void inspectionCommitteeMeetingsCovert2List(List<InspectionCommitteeMeetingsPO> inspectionCommitteeMeetingsList) {
        if (!CollectionUtils.isEmpty(inspectionCommitteeMeetingsList)) {
            inspectionCommitteeMeetingsList.forEach(committeeMeetings -> committeeMeetings.setParticipantList(paramsCovert2List(committeeMeetings.getParticipantsId(), committeeMeetings.getParticipantsName())));
        }
    }
}
