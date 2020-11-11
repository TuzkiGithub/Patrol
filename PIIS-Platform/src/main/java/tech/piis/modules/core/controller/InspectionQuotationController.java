package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionQuotationPO;
import tech.piis.modules.core.service.IInspectionQuotationService;

import javax.validation.Valid;
import java.util.List;


/**
 * 语录Controller
 *
 * @author Tuzki
 * @date 2020-11-06
 */
@RestController
@RequestMapping("/piis/quotation")
public class InspectionQuotationController extends BaseController {
    @Autowired
    private IInspectionQuotationService inspectionQuotationService;


    /**
     * 查询语录列表
     *
     * @param inspectionQuotation
     */
    @PreAuthorize("@ss.hasPermi('piis:quotation:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionQuotationPO inspectionQuotation) throws BaseException {
        startPage();
        List<InspectionQuotationPO> data = inspectionQuotationService.selectInspectionQuotationList(inspectionQuotation);
        return getDataTable(data);
    }

    /**
     * 新增语录
     *
     * @param inspectionQuotation
     */
    @PreAuthorize("@ss.hasPermi('piis:quotation:add')")
    @Log(title = "语录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionQuotationPO inspectionQuotation) {
        if (null == inspectionQuotation) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionQuotationPO.class, inspectionQuotation);
        return toAjax(inspectionQuotationService.save(inspectionQuotation));
    }

    /**
     * 修改语录
     *
     * @param inspectionQuotation
     */
    @PreAuthorize("@ss.hasPermi('piis:quotation:edit')")
    @Log(title = "语录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionQuotationPO inspectionQuotation) throws BaseException {
        if (null == inspectionQuotation) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionQuotationPO.class, inspectionQuotation);
        return toAjax(inspectionQuotationService.update(inspectionQuotation));
    }

    /**
     * 删除语录
     * quotationIds 语录ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:quotation:remove')")
    @Log(title = "语录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{quotationIds}")
    public AjaxResult remove(@PathVariable Long[] quotationIds) throws BaseException {
        return toAjax(inspectionQuotationService.deleteByInspectionQuotationIds(quotationIds));
    }

}
