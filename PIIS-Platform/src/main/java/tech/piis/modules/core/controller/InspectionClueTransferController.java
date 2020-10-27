package tech.piis.modules.core.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import tech.piis.modules.core.service.IInspectionClueTransferService;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;


/**
 * 线索移交 Controller
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/piis/transfer")
public class InspectionClueTransferController extends BaseController
{
    @Autowired
    private IInspectionClueTransferService inspectionClueTransferService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     *
     * 查询线索移交 列表
     * @param  inspectionClueTransfer
     */
    @PreAuthorize("@ss.hasPermi('core:transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionClueTransferPO inspectionClueTransfer) throws BaseException{
        startPage();
        List<InspectionClueTransferPO> data = inspectionClueTransferService.selectInspectionClueTransferList(inspectionClueTransfer);
        return getDataTable(data);
    }

    /**
     * 查询线索移交 文件
     * @param  inspectionClueTransferId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('core:transfer:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionClueTransferFile(@RequestParam("") String inspectionClueTransferId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("InspectionClueTransfer" + inspectionClueTransferId));
    }

    /**
     * 查询线索移交 总览列表
     *
     * @param planId 巡视计划ID
    */
    @PreAuthorize("@ss.hasPermi('core:transfer:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionClueTransferList(String planId) throws BaseException{
        return AjaxResult.success(inspectionClueTransferService.selectInspectionClueTransferCount(planId));
    }
    /**
     * 新增线索移交 
     * @param  inspectionClueTransfer
     */
    @PreAuthorize("@ss.hasPermi('core:transfer:add')")
    @Log(title = "线索移交 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionClueTransferPO inspectionClueTransfer) {
        if (null == inspectionClueTransfer) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionClueTransferPO.class, inspectionClueTransfer);
        return toAjax(inspectionClueTransferService.save(inspectionClueTransfer));
    }

    /**
     * 修改线索移交 
     * @param  inspectionClueTransfer
     */
    @PreAuthorize("@ss.hasPermi('core:transfer:edit')")
    @Log(title = "线索移交 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionClueTransferPO inspectionClueTransfer) throws BaseException{
        if (null == inspectionClueTransfer) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionClueTransferPO.class, inspectionClueTransfer);
        return toAjax(inspectionClueTransferService.update(inspectionClueTransfer));
    }

    /**
     * 删除线索移交 
     * clueTransferIds 线索移交 ID数组
     */
    @PreAuthorize("@ss.hasPermi('core:transfer:remove')")
    @Log(title = "线索移交 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{clueTransferIds}")
    public AjaxResult remove(@PathVariable String[] clueTransferIds) throws BaseException{
        return toAjax(inspectionClueTransferService.deleteByInspectionClueTransferIds(clueTransferIds));
    }

    /**
    * 参数类型转换
    *
    * @param inspectionClueTransfer
    */
    private void inspectionClueTransferCovert2String(InspectionClueTransferPO inspectionClueTransfer){

    }


    /**
    * 参数类型转换
    *
    * @param inspectionClueTransferList
    */
    private void inspectionClueTransferCovert2List(List<InspectionClueTransferPO> inspectionClueTransferList){

    }
}
