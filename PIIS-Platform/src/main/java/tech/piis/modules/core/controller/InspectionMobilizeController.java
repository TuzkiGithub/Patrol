package tech.piis.modules.core.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionMobilizePO;
import tech.piis.modules.core.service.IInspectionMobilizeService;

import javax.validation.Valid;

/**
 * 巡视动员 Controller
 * 
 * @author Kevin
 * @date 2020-09-27
 */
@RestController
@RequestMapping("/piis/mobilize")
public class InspectionMobilizeController extends BaseController
{
    @Autowired
    private IInspectionMobilizeService inspectionMobilizeService;

    /**
     * 查询巡视动员
     */
    @PreAuthorize("@ss.hasPermi('piis:mobilize:query')")
    @GetMapping
    public AjaxResult list(InspectionMobilizePO inspectionMobilize) {
        return null;
    }

    /**
     * 新增巡视动员
     */
    @PreAuthorize("@ss.hasPermi('piis:mobilize:add')")
    @PostMapping
    public AjaxResult add(@RequestPart("inspectionMobilize") @Valid InspectionMobilizePO inspectionMobilize,
                          @RequestPart("files") MultipartFile[] files) {
        return toAjax(0);
    }

    /**
     * 修改巡视动员 
     */
    @PreAuthorize("@ss.hasPermi('piis:mobilize:edit')")
    @Log(title = "巡视动员 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionMobilizePO inspectionMobilize) {
        return toAjax(0);
    }
}