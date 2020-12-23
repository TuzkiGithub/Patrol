package tech.piis.modules.person.controller;

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
import tech.piis.modules.person.domain.po.FullPartOrgPO;
import tech.piis.modules.person.service.IFullPartOrgService;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName : FullPartOragnController
 * Package : tech.piis.modules.person.controller
 * Description :
 * 专兼职管理
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/managment/full/part")
public class FullPartOrganController extends BaseController {

    @Autowired
    private IFullPartOrgService iFullPartOrgService;

    /**
     * 查询专兼职列表
     */
    @PreAuthorize("@ss.hasPermi('managment:full/part:query')")
    @Log(title = "特聘专员", businessType = BusinessType.OTHER)
    @GetMapping
    public AjaxResult selectByWholeName(@RequestParam(value = "orgName", required = false) String orgName) throws BaseException {
        List<FullPartOrgPO> fullPartOrgPOs = iFullPartOrgService.selectByWholeName(orgName);
        return AjaxResult.success(fullPartOrgPOs);
    }


    /**
     * 新增专兼职管理
     */
    @PreAuthorize("@ss.hasPermi('managment:full/part:add')")
    @Log(title = "特聘专员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody FullPartOrgPO fullPartOrgPO) throws BaseException {
        if (null == fullPartOrgPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(FullPartOrgPO.class, fullPartOrgPO);
        BizUtils.setCreatedTimeOperation(FullPartOrgPO.class, fullPartOrgPO);
        return toAjax(iFullPartOrgService.save(fullPartOrgPO));
    }

    /**
     * 修改专兼职管理
     */
    @PreAuthorize("@ss.hasPermi('managment:full/part:edit')")
    @Log(title = "特聘专员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody FullPartOrgPO fullPartOrgPO) throws BaseException {
        if (null == fullPartOrgPO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(FullPartOrgPO.class, fullPartOrgPO);
        BizUtils.setUpdatedTimeOperation(FullPartOrgPO.class, fullPartOrgPO);
        return toAjax(iFullPartOrgService.update(fullPartOrgPO));
    }

    /**
     * 删除专兼职管理
     */
    @PreAuthorize("@ss.hasPermi('managment:full/part:remove')")
    @Log(title = "特聘专员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable("id") String[] fullIds) throws BaseException {
        if (fullIds.length == 0) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(iFullPartOrgService.deleteByFullPartOrgId(fullIds));
    }
}
