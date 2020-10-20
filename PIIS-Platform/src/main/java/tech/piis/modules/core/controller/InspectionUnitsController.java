package tech.piis.modules.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionUnitsPO;
import tech.piis.modules.core.service.IInspectionUnitsService;

/**
 * 被巡视单位 Controller
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/piis/units")
public class InspectionUnitsController extends BaseController
{
    @Autowired
    private IInspectionUnitsService inspectionUnitsService;

    /**
     * 查询被巡视单位-组员列表
     */
    @PreAuthorize("@ss.hasPermi('piis:units:list')")
    @GetMapping("/group")
    public AjaxResult selectUnitsGroupList(Long unitsId) {
        return AjaxResult.success(inspectionUnitsService.selectUnitsMember(unitsId));
    }
}
