package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.service.IInspectionPlanService;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.controller
 * User: Tuzki
 * Date: 2020/12/8
 * Time: 17:18
 * Description:统计
 */
@RestController
@RequestMapping("piis")
public class InspectionCountController {

    @Autowired
    private IInspectionPlanService planService;

    /** 巡视巡察项目统计
     * @return
     * @throws BaseException
     */
    @GetMapping("count/project")
    public AjaxResult countProject() throws BaseException {
        return AjaxResult.success(planService.selectPiisProjectCount());
    }
}
