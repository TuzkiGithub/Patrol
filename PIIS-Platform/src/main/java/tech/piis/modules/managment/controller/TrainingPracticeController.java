package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.common.utils.DateUtils;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.managment.domain.po.TrainingPracticePO;
import tech.piis.modules.managment.service.ITrainingPracticeService;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName : TrainingPracticeController
 * Package : tech.piis.modules.managment.controller
 * Description :C以干代训 controller
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/managment/practice")
public class TrainingPracticeController extends BaseController {
    @Autowired
    private ITrainingPracticeService iTrainingPracticeService;

    /**
     * 以干代训总览
     */
    @PreAuthorize("@ss.hasPermi('managment:practice:query')")
    @Log(title = "以干代训", businessType = BusinessType.OTHER)
    @GetMapping("/count")
    public TableDataInfo count(TrainingPracticePO trainingPracticePO) throws BaseException {
        startPage();
        List<TrainingPracticePO> trainingPracticePOList = iTrainingPracticeService.selectPracticeListByOrgId();
        return getDataTable(trainingPracticePOList);
    }

    /**
     * 根据以干代训ID查询以干代训
     */
    @PreAuthorize("@ss.hasPermi('managment:practice:query')")
    @Log(title = "以干代训", businessType = BusinessType.OTHER)
    @GetMapping("/{id}")
    public AjaxResult getRecommendById(@PathVariable String id) throws BaseException {
        if (StringUtils.isEmpty(id)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        TrainingPracticePO trainingPracticePO = iTrainingPracticeService.getRecommendById(id);
        return AjaxResult.success(trainingPracticePO);
    }

    /**
     * 根据查询条件动态查询以干代训记录
     */
    @PreAuthorize("@ss.hasPermi('managment:practice:query')")
    @Log(title = "以干代训", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    public TableDataInfo getRecommendByConditions(TrainingPracticePO trainingPracticePO) throws BaseException {
        startPage();
        List<TrainingPracticePO> practicePOListList = iTrainingPracticeService.selectPracticeList(trainingPracticePO);
        return getDataTable(practicePOListList);
    }

    /**
     * 新增以干代训
     */
    @PreAuthorize("@ss.hasPermi('managment:practice:add')")
    @Log(title = "以干代训", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody TrainingPracticePO trainingPracticePO) throws BaseException {
        if (null == trainingPracticePO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(TrainingPracticePO.class, trainingPracticePO);
        BizUtils.setCreatedTimeOperation(TrainingPracticePO.class, trainingPracticePO);
        return toAjax(iTrainingPracticeService.saveRecommendBest(trainingPracticePO));
    }

    /**
     * 删除以干代训
     */
    @PreAuthorize("@ss.hasPermi('managment:practice:remove')")
    @Log(title = "以干代训", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) throws BaseException {
        if (ids.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iTrainingPracticeService.delRecommendByIds(ids));
    }

    /**
     * 修改以干代训
     */
    @PreAuthorize("@ss.hasPermi('managment:practice:edit')")
    @Log(title = "以干代训", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody TrainingPracticePO trainingPracticePO) throws BaseException {
        trainingPracticePO.setUpdatedBy(SecurityUtils.getUsername());
        trainingPracticePO.setUpdatedTime(DateUtils.getNowDate());
        return toAjax(iTrainingPracticeService.update(trainingPracticePO));
    }
}
