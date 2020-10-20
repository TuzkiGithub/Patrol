package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionSpecialReportPO;
import tech.piis.modules.core.service.IInspectionSpecialReportService;
import tech.piis.modules.core.service.IPiisDocumentService;

import java.util.List;

/**
 * 听取专题报告 Controller
 *
 * @author Kevin
 * @date 2020-10-12
 */
@RestController
@RequestMapping("/piis/report")
public class InspectionSpecialReportController extends BaseController {
    @Autowired
    private IInspectionSpecialReportService inspectionSpecialReportService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询听取专题报告 列表
     */
    @PreAuthorize("@ss.hasPermi('piis:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionSpecialReportPO inspectionSpecialReport) {
        startPage();
        List<InspectionSpecialReportPO> data = inspectionSpecialReportService.selectSpecialReport(inspectionSpecialReport);
        specialReportCovert2List(data);
        return getDataTable(data);
    }


    /**
     * 查询专题报告文件
     *
     * @param specialReportId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('piis:report:query')")
    @GetMapping("/file")
    public AjaxResult findSpecialReportFile(String specialReportId) throws Exception {
        return AjaxResult.success(documentService.getFileListByBizId(specialReportId));
    }

    /**
     * 查询听取专题报告总览列表
     */
    @PreAuthorize("@ss.hasPermi('piis:report:query')")
    @GetMapping("/count")
    public AjaxResult countList(InspectionSpecialReportPO inspectionSpecialReport) {
        return AjaxResult.success(inspectionSpecialReportService.selectSpecialReportCount(inspectionSpecialReport.getPlanId())
        );
    }


    /**
     * 新增听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:report:add')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionSpecialReportPO inspectionSpecialReport) throws Exception {
        BizUtils.setCreatedOperation(InspectionSpecialReportPO.class, inspectionSpecialReport);
        specialReportCovert2String(inspectionSpecialReport);
        return toAjax(inspectionSpecialReportService.save(inspectionSpecialReport));
    }

    /**
     * 修改听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:report:edit')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionSpecialReportPO inspectionSpecialReport) throws Exception {
        if (null == inspectionSpecialReport.getSpecialReportId()) {
            return AjaxResult.error(BizConstants.SPECIAL_REPORT_ID_NULL);
        }
        specialReportCovert2String(inspectionSpecialReport);
        BizUtils.setUpdatedOperation(InspectionSpecialReportPO.class, inspectionSpecialReport);
        return toAjax(inspectionSpecialReportService.update(inspectionSpecialReport));
    }

    /**
     * 删除听取专题报告
     */
    @PreAuthorize("@ss.hasPermi('piis:report:remove')")
    @Log(title = "听取专题报告 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{specialReportIds}")
    public AjaxResult remove(@PathVariable Long[] specialReportIds) {
        return toAjax(inspectionSpecialReportService.deleteBySpecialReportIds(specialReportIds));
    }


    /**
     * 参数类型转换
     *
     * @param specialReport
     */
    private void specialReportCovert2String(InspectionSpecialReportPO specialReport) {
        if (null != specialReport) {
            specialReport.setReporterId(paramsCovert2String(specialReport.getReporter()).get(0));
            specialReport.setReporterName(paramsCovert2String(specialReport.getReporter()).get(1));
            specialReport.setInspectionGroupPersonId(paramsCovert2String(specialReport.getInspectionGroupPersons()).get(0));
            specialReport.setInspectionGroupPersonName(paramsCovert2String(specialReport.getInspectionGroupPersons()).get(1));
            specialReport.setReportPersonId(paramsCovert2String(specialReport.getParticipants()).get(0));
            specialReport.setReportPersonName(paramsCovert2String(specialReport.getParticipants()).get(1));
        }
    }

    /**
     * 参数类型转换
     *
     * @param specialReportList
     */
    private void specialReportCovert2List(List<InspectionSpecialReportPO> specialReportList) {
        if (!CollectionUtils.isEmpty(specialReportList)) {
            specialReportList.forEach(specialReport -> {
                specialReport.setReporter(paramsCovert2List(specialReport.getReporterId(), specialReport.getReporterName()));
                specialReport.setInspectionGroupPersons(paramsCovert2List(specialReport.getInspectionGroupPersonId(), specialReport.getInspectionGroupPersonName()));
                specialReport.setParticipants(paramsCovert2List(specialReport.getReportPersonId(), specialReport.getReportPersonName()));
            });
        }
    }
}
