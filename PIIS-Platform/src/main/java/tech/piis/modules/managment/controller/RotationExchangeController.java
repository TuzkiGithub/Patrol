package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.utils.DateUtils;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.SecurityUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.managment.domain.RotationExchangePO;
import tech.piis.modules.managment.service.IRotationExchangeService;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName : RotationExchangeController
 * Package : tech.piis.modules.managment.controller
 * Description :
 * 轮岗交流controller
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/managment/rotation")
public class RotationExchangeController extends BaseController {

    @Autowired
    private IRotationExchangeService iRotationExchangeService;


    /**
     * 轮岗交流总览
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:query')")
    @GetMapping("/count")
    public TableDataInfo count() {
        startPage();
        List<RotationExchangePO> rotationExchangePOS = iRotationExchangeService.selectRecommendListByOrgId();
        return getDataTable(rotationExchangePOS);
    }

    /**
     * 根据查询条件动态查询轮岗交流记录
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:query')")
    @GetMapping("/list")
    public TableDataInfo getRecommendByConditions(RotationExchangePO rotationExchangePO) throws Exception {
        startPage();
        List<RotationExchangePO> rotationList = iRotationExchangeService.selectRotationList(rotationExchangePO);
        //todo 判断rotationList查询结果为0时如何处理
        return getDataTable(rotationList);
    }

    /**
     * 新增轮岗交流
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:add')")
    @PostMapping
    public AjaxResult add(@Valid @RequestBody RotationExchangePO rotationExchangePO) throws Exception {
        if (null == rotationExchangePO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(RotationExchangePO.class, rotationExchangePO);
        BizUtils.setCreatedTimeOperation(RotationExchangePO.class, rotationExchangePO);
        return toAjax(iRotationExchangeService.saveRotationExchange(rotationExchangePO));
    }

    /**
     * 删除择优推荐
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        if (ids.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iRotationExchangeService.delRotationByIds(ids));
    }

    /**
     * 修改轮岗交流
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:edit')")
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody RotationExchangePO rotationExchangePO) throws Exception{
        rotationExchangePO.setUpdatedBy(SecurityUtils.getUsername());
        rotationExchangePO.setUpdatedTime(DateUtils.getNowDate());
        return toAjax(iRotationExchangeService.update(rotationExchangePO));
    }
}
