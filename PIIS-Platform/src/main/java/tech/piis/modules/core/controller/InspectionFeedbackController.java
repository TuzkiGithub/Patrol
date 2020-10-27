package tech.piis.modules.core.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import tech.piis.modules.core.service.IInspectionFeedbackService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;


/**
 * 反馈意见 Controller
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/piis/feedback")
public class InspectionFeedbackController extends BaseController
{
    @Autowired
    private IInspectionFeedbackService inspectionFeedbackService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询反馈意见 列表
     * @param  inspectionFeedback
     */
    @PreAuthorize("@ss.hasPermi('core:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionFeedbackPO inspectionFeedback) throws BaseException{
        startPage();
        List<InspectionFeedbackPO> data = inspectionFeedbackService.selectInspectionFeedbackList(inspectionFeedback);
        return getDataTable(data);
    }

    /**
     * 查询反馈意见 文件
     * @param  inspectionFeedbackId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('core:feedback:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionFeedbackFile(@RequestParam("") String inspectionFeedbackId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("InspectionFeedback" + inspectionFeedbackId));
    }

    /**
     * 查询反馈意见 总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('core:feedback:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionFeedbackList(String planId) throws BaseException{
        return AjaxResult.success(inspectionFeedbackService.selectInspectionFeedbackCount(planId));
    }
    /**
     * 新增反馈意见 
     * @param  inspectionFeedback
     */
    @PreAuthorize("@ss.hasPermi('core:feedback:add')")
    @Log(title = "反馈意见 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionFeedbackPO inspectionFeedback) {
        if (null == inspectionFeedback) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionFeedbackPO.class, inspectionFeedback);
        return toAjax(inspectionFeedbackService.save(inspectionFeedback));
    }

    /**
     * 修改反馈意见 
     * @param  inspectionFeedback
     */
    @PreAuthorize("@ss.hasPermi('core:feedback:edit')")
    @Log(title = "反馈意见 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionFeedbackPO inspectionFeedback) throws BaseException{
        if (null == inspectionFeedback) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionFeedbackPO.class, inspectionFeedback);
        return toAjax(inspectionFeedbackService.update(inspectionFeedback));
    }

    /**
     * 删除反馈意见 
     * feedbackIds 反馈意见 ID数组
     */
    @PreAuthorize("@ss.hasPermi('core:feedback:remove')")
    @Log(title = "反馈意见 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable String[] feedbackIds) throws BaseException{
        return toAjax(inspectionFeedbackService.deleteByInspectionFeedbackIds(feedbackIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionFeedback
    */
    private void inspectionFeedbackCovert2String(InspectionFeedbackPO inspectionFeedback){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionFeedbackList
    */
    private void inspectionFeedbackCovert2List(List<InspectionFeedbackPO> inspectionFeedbackList){

    }
}
