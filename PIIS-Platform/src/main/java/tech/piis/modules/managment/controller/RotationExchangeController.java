package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import tech.piis.modules.managment.domain.po.RotationExchangePO;
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
    @Log(title = "轮岗交流", businessType = BusinessType.OTHER)
    @GetMapping("/count")
    public TableDataInfo count() throws BaseException {
        startPage();
        List<RotationExchangePO> rotationExchangePOS = iRotationExchangeService.selectRecommendListByOrgId();
        return getDataTable(rotationExchangePOS);
    }

    /**
     * 根据查询条件动态查询轮岗交流记录
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:query')")
    @Log(title = "轮岗交流", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    public TableDataInfo getRecommendByConditions(RotationExchangePO rotationExchangePO) throws BaseException {
        startPage();
        List<RotationExchangePO> rotationList = iRotationExchangeService.selectRotationList(rotationExchangePO);
        return getDataTable(rotationList);
    }

    /**
     * 新增轮岗交流
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:add')")
    @Log(title = "轮岗交流", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody RotationExchangePO rotationExchangePO) throws BaseException {
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
    @Log(title = "轮岗交流", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) throws BaseException {
        if (ids.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iRotationExchangeService.delRotationByIds(ids));
    }

    /**
     * 修改轮岗交流
     */
    @PreAuthorize("@ss.hasPermi('managment:rotation:edit')")
    @Log(title = "轮岗交流", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody RotationExchangePO rotationExchangePO) throws BaseException {
        rotationExchangePO.setUpdatedBy(SecurityUtils.getUsername());
        rotationExchangePO.setUpdatedTime(DateUtils.getNowDate());
        return toAjax(iRotationExchangeService.update(rotationExchangePO));
    }
}
