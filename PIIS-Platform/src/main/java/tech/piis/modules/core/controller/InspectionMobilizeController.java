package tech.piis.modules.core.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.dto.MobilizedAttendeeFileDTO;
import tech.piis.modules.core.domain.po.InspectionMobilizePO;
import tech.piis.modules.core.service.IInspectionMobilizeAttendeeService;
import tech.piis.modules.core.service.IInspectionMobilizeService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;

/**
 * 巡视动员 Controller
 *
 * @author Kevin
 * @date 2020-09-27
 */
@RestController
@RequestMapping("/piis/mobilize")
public class InspectionMobilizeController extends BaseController {
    @Autowired
    private IInspectionMobilizeService inspectionMobilizeService;

    @Autowired
    private IInspectionMobilizeAttendeeService inspectionMobilizeAttendeeService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 获取巡视动员文件列表
     * @param mobilizeId
     * @return
     * @throws Exception
     */
    @PreAuthorize("@ss.hasPermi('piis:mobilize:query')")
    @GetMapping("file")
    public AjaxResult findMobilizeFile(String mobilizeId) throws Exception {
        if (StringUtils.isEmpty(mobilizeId)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        MobilizedAttendeeFileDTO mobilizedAttendeeFileDTO = new MobilizedAttendeeFileDTO()
                .setMeetingFiles(documentService.getFileListByBizId(mobilizeId))
                .setMobilizeAttendeeList(inspectionMobilizeAttendeeService.selectAttendeeDocuments(mobilizeId));
        return AjaxResult.success(mobilizedAttendeeFileDTO);
    }

    /**
     * 新增巡视动员
     */
    @PreAuthorize("@ss.hasPermi('piis:mobilize:add')")
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionMobilizePO inspectionMobilize) throws Exception {
        if (null == inspectionMobilize) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionMobilizeService.saveInspectionMobilize(inspectionMobilize));
    }

    /**
     * 修改巡视动员
     */
    @PreAuthorize("@ss.hasPermi('piis:mobilize:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionMobilizePO inspectionMobilize) throws Exception {
        if (null == inspectionMobilize) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionMobilizeService.updateInspectionMobilize(inspectionMobilize));
    }
}