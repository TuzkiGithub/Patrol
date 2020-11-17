package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.utils.DateUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.managment.domain.RecommendBestPO;
import tech.piis.modules.managment.service.IRecommendBestService;
import tech.piis.modules.system.service.ISysDeptService;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName : RecommendBestController
 * Package : tech.piis.modules.managment.controller
 * Description :
 * 择优推荐 controller
 *
 * @author : chenhui@xvco.com
 */

@RestController
@RequestMapping("/managment/recommend")
public class RecommendBestController extends BaseController {

    @Autowired
    private IRecommendBestService iRecommendBestService;
    @Autowired
    private ISysDeptService iSysDeptService;

    /**
     * 择优推荐总览
     */
    @PreAuthorize("@ss.hasPermi('managment:recommend:query')")
    @GetMapping("/count")
    public TableDataInfo count(){

        startPage();
        List<RecommendBestPO> recommendBestPOList = iRecommendBestService.selectRecommendListByOrgId();
        return getDataTable(recommendBestPOList);
    }
    /**
     * 根据择优推荐ID查询择优推荐
     */
    @PreAuthorize("@ss.hasPermi('managment:recommend:query')")
    @GetMapping("/{id}")
    public AjaxResult getRecommendById(@PathVariable String id) throws Exception {
        if (StringUtils.isEmpty(id)){
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        RecommendBestPO recommendBestPO = iRecommendBestService.getRecommendById(id);
        return AjaxResult.success(recommendBestPO);
    }

    /**
     * 根据查询条件动态查询择优推荐记录
     */
    @PreAuthorize("@ss.hasPermi('managment:recommend:query')")
    @GetMapping("/list")
    public TableDataInfo getRecommendByConditions(RecommendBestPO recommendBestPO) throws Exception {
        startPage();
        List<RecommendBestPO> recommendList = iRecommendBestService.selectRecommendList(recommendBestPO);
        return getDataTable(recommendList);
    }

    /**
     * 新增择优推荐
     */
    @PreAuthorize("@ss.hasPermi('managment:recommend:add')")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody RecommendBestPO recommendBestPO) throws Exception {
        if (null == recommendBestPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(RecommendBestPO.class, recommendBestPO);
        BizUtils.setCreatedTimeOperation(RecommendBestPO.class, recommendBestPO);
        return toAjax(iRecommendBestService.saveRecommendBest(recommendBestPO));
    }

    /**
     * 删除择优推荐
     */
    @PreAuthorize("@ss.hasPermi('managment:recommend:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        if (ids.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iRecommendBestService.delRecommendByIds(ids));
    }

    /**
     * 修改择优推荐
     */
    @PreAuthorize("@ss.hasPermi('managment:recommend:edit')")
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody RecommendBestPO recommendBestPO){
        recommendBestPO.setUpdatedBy(SecurityUtils.getUsername());
        recommendBestPO.setUpdatedTime(DateUtils.getNowDate());
        return toAjax(iRecommendBestService.update(recommendBestPO));
    }

}
