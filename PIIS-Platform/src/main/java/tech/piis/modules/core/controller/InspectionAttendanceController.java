package tech.piis.modules.core.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionAttendancePO;
import tech.piis.modules.core.service.IInspectionAttendanceService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;



/**
 * 参会情况Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/attendance")
public class InspectionAttendanceController extends BaseController
{
    @Autowired
    private IInspectionAttendanceService inspectionAttendanceService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询参会情况列表
     * @param  inspectionAttendance
     */
    @PreAuthorize("@ss.hasPermi('piis:attendance:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionAttendancePO inspectionAttendance) throws BaseException{
        startPage();
        List<InspectionAttendancePO> data = inspectionAttendanceService.selectInspectionAttendanceList(inspectionAttendance);
        return getDataTable(data);
    }

    /**
     * 查询参会情况文件
     * @param  inspectionAttendanceId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:attendance:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionAttendanceFile(String inspectionAttendanceId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId(inspectionAttendanceId));
    }

    /**
     * 查询参会情况总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('piis:attendance:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionAttendanceList(String planId) throws BaseException{
        return AjaxResult.success(inspectionAttendanceService.selectInspectionAttendanceCount(planId));
    }
    /**
     * 新增参会情况
     * @param  inspectionAttendance
     */
    @PreAuthorize("@ss.hasPermi('piis:attendance:add')")
    @Log(title = "参会情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InspectionAttendancePO inspectionAttendance) {
        if (null == inspectionAttendance) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionAttendanceService.save(inspectionAttendance));
    }

    /**
     * 修改参会情况
     * @param  inspectionAttendance
     */
    @PreAuthorize("@ss.hasPermi('piis:attendance:edit')")
    @Log(title = "参会情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionAttendancePO inspectionAttendance) throws BaseException{
        if (null == inspectionAttendance) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionAttendancePO.class, inspectionAttendance);
        return toAjax(inspectionAttendanceService.update(inspectionAttendance));
    }

    /**
     * 删除参会情况
     * attendanceIds 参会情况ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:attendance:remove')")
    @Log(title = "参会情况", businessType = BusinessType.DELETE)
	@DeleteMapping("/{attendanceIds}")
    public AjaxResult remove(@PathVariable Long[] attendanceIds) throws BaseException{
        return toAjax(inspectionAttendanceService.deleteByInspectionAttendanceIds(attendanceIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionAttendance
    */
    private void specialReportCovert2String(InspectionAttendancePO inspectionAttendance){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionAttendanceList
    */
    private void specialReportCovert2List(List<InspectionAttendancePO> inspectionAttendanceList){

    }
}
