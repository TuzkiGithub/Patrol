package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.GenConstants;
import tech.piis.common.constant.PiisConstants;
import tech.piis.common.constant.ResultEnum;
import tech.piis.common.utils.DateUtils;
import tech.piis.common.utils.StringUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionPlanPO;
import tech.piis.modules.core.service.IInspectionPlanService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;


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
    public TableDataInfo list(InspectionPlanPO inspectionPlanPO) {
        if (null != inspectionPlanPO) {
            //模糊查询时不分页
            if (!StringUtils.isEmpty(inspectionPlanPO.getPlanName())) {
                inspectionPlanPO.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionPlanPO.setPageSize(GenConstants.MAX_PAGE_SIZE);
            }

            Integer planType = inspectionPlanPO.getPlanType();
            if(null == planType){
                return new TableDataInfo()
                        .setCode(ResultEnum.FAILED.getCode())
                        .setMsg(BizConstants.PIIS_TYPE_NULL);
            } else if(planType == PiisConstants.PIIS_TYPE && null == inspectionPlanPO.getPlanCompanyId()){
                return new TableDataInfo()
                        .setCode(ResultEnum.FAILED.getCode())
                        .setMsg(BizConstants.COMPANY_ID_NULL);
            }
            if (null == inspectionPlanPO.getPageNum() || null == inspectionPlanPO.getPageSize()) {
                inspectionPlanPO.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionPlanPO.setPageSize(GenConstants.DEFAULT_PAGE_SIZE);
            } else {
                inspectionPlanPO.setPageNum(inspectionPlanPO.getPageNum() * inspectionPlanPO.getPageSize());
            }
        }
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(inspectionPlanService.selectPlanList(inspectionPlanPO))
                .setTotal(inspectionPlanService.selectCount(inspectionPlanPO));
    }

    /**
     * 统计一级子公司巡察情况
     * @return
     */
    @PreAuthorize("@ss.hasPermi('piis:plan:query')")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public AjaxResult countCompany(InspectionPlanPO planPO) {
        return AjaxResult.success(inspectionPlanService.selectCountByCompany(planPO));
    }

    /**
     * 获取巡视文件
     *
     * @param planId
     * @return
     * @throws Exception
     */
    @PreAuthorize("@ss.hasPermi('piis:plan:query')")
    @GetMapping("file")
    public AjaxResult getPlanFile(String planId) throws Exception {
        return AjaxResult.success(documentService.getFileListByBizId(planId));
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
    public AjaxResult edit(@RequestBody InspectionPlanPO inspectionPlan) throws Exception {
        if (null == inspectionPlan) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionPlan.setUpdatedBy(SecurityUtils.getUsername());
        inspectionPlan.setUpdatedTime(DateUtils.getNowDate());
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
