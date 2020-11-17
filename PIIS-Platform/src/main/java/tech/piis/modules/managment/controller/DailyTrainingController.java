package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.managment.domain.DailyTrainingPO;
import tech.piis.modules.managment.service.IDailyTrainingService;
import javax.validation.Valid;
import java.util.List;

/**
 * ClassName : DailyTrainingController
 * Package : tech.piis.modules.managment.controller
 * Description :
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
    @GetMapping("/count")
    public TableDataInfo count(DailyTrainingPO dailyTrainingPO) throws Exception {
        startPage();
        List<DailyTrainingPO> dailyTrainingPOS =  iDailyTrainingService.selectDailyTrainingByOrgId();
        return getDataTable(dailyTrainingPOS);
    }


    /**
     * 日常培训列表
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:query')")
    @GetMapping("/list")
    public TableDataInfo list(DailyTrainingPO dailyTrainingPO) throws Exception {
        startPage();
        List<DailyTrainingPO> dailyTrainingPOS =  iDailyTrainingService.selectDailyTrainingList(dailyTrainingPO);
        return getDataTable(dailyTrainingPOS);
    }

    /**
     *  根据ID查询日常培训 及其关联培训课程信息与培训人员信息
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:query')")
    @GetMapping("/info")
    public AjaxResult selectDailyTrainingInfo(@RequestParam(value = "dailyId",required = true)String  dailyId) throws Exception {
        DailyTrainingPO dailyTrainingPO = iDailyTrainingService.selectDailyTrainingInfo(dailyId);
        return AjaxResult.success(dailyTrainingPO);
    }

    /**
     * 新增日常培训
     */
    @PreAuthorize("@ss.hasPermi('managment:daily/training:add')")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody DailyTrainingPO dailyTrainingPO) throws Exception {
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
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody DailyTrainingPO dailyTrainingPO) throws Exception {
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
    @DeleteMapping("/{dailyId}")
    public AjaxResult remove(@PathVariable(value = "dailyId") String id) throws Exception {
        if (null == id) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iDailyTrainingService.delDailyTrainingById(id));
    }
}
