package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.ResultEnum;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.managment.domain.RecommendBestPO;
import tech.piis.modules.managment.service.RecommendBestService;

import javax.validation.Valid;

/**
 * ClassName : RecommendBestController
 * Package : tech.piis.modules.managment.controller
 * Description :
 * 择优推荐 controller
 *
 * @author : chenhui@xvco.com
 */

@RestController
@RequestMapping("/piis/recommend")
public class RecommendBestController extends BaseController {

    @Autowired
    private RecommendBestService recommendBestService;


    /**
     * 根据择优推荐ID查询择优推荐
     */
    @PreAuthorize("@ss.hasPermi('piis:recommend:query')")
    @GetMapping("/{id}")
    public AjaxResult getRecommendById(@PathVariable String id) throws Exception {
        if (StringUtils.isEmpty(id)){
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        RecommendBestPO recommendBestPO = recommendBestService.getRecommendById(id);
        return AjaxResult.success(recommendBestPO);
    }

    /**
     * 根据查询条件动态查询择优推荐记录
     */
    @PreAuthorize("@ss.hasPermi('piis:recommend:query')")
    @GetMapping
    public TableDataInfo getRecommendByConditions(RecommendBestPO recommendBestPO) throws Exception {
        return new TableDataInfo()
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setCode(ResultEnum.SUCCESS.getCode())
                .setRows(recommendBestService.selectRecommendList(recommendBestPO))
                .setTotal(recommendBestService.selectCount(recommendBestPO));
    }

    /**
     * 新增择优推荐
     */
    @PreAuthorize("@ss.hasPermi('piis:recommend:add')")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody RecommendBestPO recommendBestPO) throws Exception {
        if (null == recommendBestPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(recommendBestService.saveRecommendBest(recommendBestPO));
    }

    /**
     * 删除择优推荐
     */
    @PreAuthorize("@ss.hasPermi('piis:recommend:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        if (ids.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.recommendBestService.delRecommendByIds(ids));
    }


}
