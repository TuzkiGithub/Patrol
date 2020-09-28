package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.GenConstants;
import tech.piis.common.constant.ResultEnum;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.dto.InspectionPlanSaveDTO;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.List;


/**
 * 巡视计划 Controller
 *
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/piis/plan")
public class InspectionPlanController extends BaseController {
    @Autowired
    private IInspectionPlanService inspectionPlanService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询巡视计划列表
     */
    @PreAuthorize("@ss.hasPermi('piis:plan:list')")
    @GetMapping("/list")
    public AjaxResult list(InspectionPlanPO inspectionPlanPO) {
        if (null != inspectionPlanPO) {
            if (null == inspectionPlanPO.getPageNum() || null == inspectionPlanPO.getPageSize()) {
                inspectionPlanPO.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionPlanPO.setPageSize(GenConstants.DEFAULT_PAGE_SIZE);
            }
        }
        return AjaxResult.success(inspectionPlanService.selectPlanList(inspectionPlanPO));
    }

    @GetMapping("file")
    public AjaxResult getPlanFile(String planId) throws Exception{
        return AjaxResult.success(documentService.getPlanFileList(new PiisDocumentPO().setObjectId(planId)));
    }

    /**
     * 新增巡视计划
     */
    @PreAuthorize("@ss.hasPermi('piis:plan:add')")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody InspectionPlanPO inspectionPlanPO) throws Exception {
        if (null == inspectionPlanPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionPlanService.savePlan(inspectionPlanPO));
    }

    /**
     * 修改巡视计划
     */
    @PreAuthorize("@ss.hasPermi('piis:plan:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionPlanPO inspectionPlan) throws Exception{
        if (null == inspectionPlan) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(inspectionPlanService.editPlan(inspectionPlan));
    }

    /**
     * 删除巡视计划
     */
    @PreAuthorize("@ss.hasPermi('piis:plan:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        if (ids.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.inspectionPlanService.delPlanByIds(ids));
    }
}
