package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.mapper.InspectionFilingMapper;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.controller
 * User: Tuzki
 * Date: 2020/11/3
 * Time: 19:12
 * Description:
 */
@RestController
@RequestMapping("piis/filing")
public class InspectionFilingController {

    @Autowired
    private InspectionFilingMapper inspectionFilingMapper;

    @PreAuthorize("@ss.hasPermi('piis:filing:perms')")
    @RequestMapping("count")
    public AjaxResult selectFiling(String planId) {
        return AjaxResult.success(inspectionFilingMapper.selectFilingByPlanId(planId));
    }
}
