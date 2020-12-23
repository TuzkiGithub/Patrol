package tech.piis.modules.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.vo.PlanMemberCountVO;
import tech.piis.modules.core.service.IInspectionPlanService;

/**
 * ClassName : TalentOverviewController
 * Package : tech.piis.modules.person.controller
 * Description :
 * 人才库总览
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/managment/talent/overview")
public class TalentOverviewController {

    @Autowired
    private IInspectionPlanService iInspectionPlanService;

    /**
     * 人才库预览
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('managment:talent/overview:query')")
    @Log(title = "人才库总览", businessType = BusinessType.OTHER)
    @GetMapping
    public AjaxResult selectMemberCountByTime(PlanMemberCountVO planMemberCountVO) throws BaseException {
        return AjaxResult.success(iInspectionPlanService.selectMemberCountByTime(planMemberCountVO));
    }
}
