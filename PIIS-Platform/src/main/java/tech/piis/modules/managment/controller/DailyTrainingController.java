package tech.piis.modules.managment.controller;

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
import tech.piis.modules.managment.domain.po.DailyTrainingPO;
import tech.piis.modules.managment.service.IDailyTrainingService;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName : DailyTrainingController
 * Package : tech.piis.modules.managment.controller
 * Description :
 * 专业培训controller
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/managment/daily/training")
public class DailyTrainingController extends BaseController {

    @Autowired
    private IDailyTrainingService iDailyTrainingService;

    /**
     * 日常培训信息总览
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:query')")
    @Log(title = "专业培训", businessType = BusinessType.OTHER)
    @GetMapping("/count")
    public TableDataInfo count(DailyTrainingPO dailyTrainingPO) throws BaseException {
        startPage();
        List<DailyTrainingPO> dailyTrainingPOS = iDailyTrainingService.selectDailyTrainingByOrgId();
        return getDataTable(dailyTrainingPOS);
    }


    /**
     * 日常培训列表
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:query')")
    @Log(title = "专业培训", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    public TableDataInfo list(DailyTrainingPO dailyTrainingPO) throws BaseException {
        startPage();
        List<DailyTrainingPO> dailyTrainingPOS = iDailyTrainingService.selectDailyTrainingList(dailyTrainingPO);
        return getDataTable(dailyTrainingPOS);
    }

    /**
     * 根据ID查询日常培训 及其关联培训课程信息与培训人员信息
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:query')")
    @Log(title = "专业培训", businessType = BusinessType.OTHER)
    @GetMapping("/info")
    public AjaxResult selectDailyTrainingInfo(@RequestParam(value = "dailyId", required = true) String dailyId) throws BaseException {
        DailyTrainingPO dailyTrainingPO = iDailyTrainingService.selectDailyTrainingInfo(dailyId);
        return AjaxResult.success(dailyTrainingPO);
    }

    /**
     * 新增日常培训
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:add')")
    @Log(title = "专业培训", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody DailyTrainingPO dailyTrainingPO) throws BaseException {
        if (null == dailyTrainingPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(DailyTrainingPO.class, dailyTrainingPO);
        BizUtils.setCreatedTimeOperation(DailyTrainingPO.class, dailyTrainingPO);
        return toAjax(iDailyTrainingService.saveDailyTraining(dailyTrainingPO));
    }

    /**
     * 修改日常培训
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:edit')")
    @Log(title = "专业培训", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody DailyTrainingPO dailyTrainingPO) throws BaseException {
        if (null == dailyTrainingPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(DailyTrainingPO.class, dailyTrainingPO);
        BizUtils.setUpdatedTimeOperation(DailyTrainingPO.class, dailyTrainingPO);
        return toAjax(iDailyTrainingService.updateDailyTraining(dailyTrainingPO));
    }

    /**
     * 删除日常培训
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:remove')")
    @Log(title = "专业培训", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dailyId}")
    public AjaxResult remove(@PathVariable(value = "dailyId") String id) throws BaseException {
        if (null == id) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iDailyTrainingService.delDailyTrainingById(id));
    }
}
