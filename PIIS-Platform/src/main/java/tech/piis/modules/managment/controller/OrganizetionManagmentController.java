package tech.piis.modules.managment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.modules.managment.service.FullPartOrgService;
import tech.piis.modules.managment.service.OrganizationService;
import tech.piis.modules.system.service.ISysDeptService;


/**
 * ClassName : OrganizetionManagmentController
 * Package : tech.piis.modules.managment.controller
 * Description : 机构管理Controller
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/piis/organizetion")
public class OrganizetionManagmentController extends BaseController {


    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private FullPartOrgService fullPartOrgService;
    @Autowired
    private ISysDeptService iSysDeptService;


    @PreAuthorize("@ss.hasPermi('piis:organizetionManagment:query')")
    @GetMapping
    public Object getPageInfo(@RequestParam(value = "organId",required = false)String organId) {
        Object object = organizationService.getPageInfo(organId);
        return object;
    }


}
