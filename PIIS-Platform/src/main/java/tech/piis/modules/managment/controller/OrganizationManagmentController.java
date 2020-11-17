package tech.piis.modules.managment.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.managment.domain.LeadingGroupFoundPO;
import tech.piis.modules.managment.domain.MemberResumePO;
import tech.piis.modules.managment.domain.vo.InspectionInfoVO;
import tech.piis.modules.managment.service.ILeadingGroupFoundService;
import tech.piis.modules.managment.service.IMemberResumeService;
import tech.piis.modules.managment.service.IOrganizationService;

import java.util.List;
import java.util.Map;


/**
 * ClassName : OrganizationManagmentController
 * Package : tech.piis.modules.managment.controller
 * Description : 机构管理Controller
 *
 * @author : chenhui@xvco.com
 */
@RestController
@RequestMapping("/managment/organizetion")
public class OrganizationManagmentController extends BaseController {


    @Autowired
    private IOrganizationService iOrganizationService;
    @Autowired
    private IMemberResumeService iMemberResumeService;
    @Autowired
    private ILeadingGroupFoundService iLeadingGroupFoundService;

    /*@PreAuthorize("@ss.hasPermi('piis:organizetionManagment:query')")
    @GetMapping
    public Object getPageInfo(@RequestParam(value = "organId",required = false)String organId) {
        Object object = organizationService.getPageInfo(organId);
        return object;
    }*/

    /**
     * 新增人员履历
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:add')")
    @PostMapping
    public AjaxResult add(MemberResumePO memberResumePO){
        if (null ==memberResumePO){
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
       return toAjax(iMemberResumeService.save(memberResumePO));
    }

    /**
     * 查询人员基本情况(家庭基本情况、专业培训情况、巡视巡察参与情况)
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @GetMapping("/{memberId}")
    public AjaxResult selectBasicInfoByMemberId(@PathVariable(value = "memberId")String memberId){
        if (StringUtils.isBlank(memberId)){
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        Map<String,Object> resultMap = iOrganizationService.selectBasicInfoByMemberId(memberId);
        return AjaxResult.success(resultMap);
    }

    /**
     * 新增子公司巡察领导小组、巡察领导小组成员表
     *
     * 机构编号、机构名称、成立文件编号、List<巡察领导小组成员信息>(姓名、职务、角色、任命文件编号)
     */
    /*@PreAuthorize("@ss.hasPermi('managment:organizetion:add')")
    @PostMapping
    public AjaxResult addInspectionOfficeMember(LeadingGroupFoundPO leadingGroupFoundPO){
        if (null == leadingGroupFoundPO){
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(iLeadingGroupFoundService.save(leadingGroupFoundPO));
    }*/

    /**
     * 子公司巡察机构、人员数量预览
     */
    /*@PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @GetMapping
    public AjaxResult selectInspectionInfo(){
        List<InspectionInfoVO> inspectionInfoVOList = iLeadingGroupFoundService.queryLeadingInspectionInfo();
        return AjaxResult.success(inspectionInfoVOList);
    }*/

    /**
     * 新增巡视/巡察领导小组、新增巡视/巡察领导小组成员
     */
    /*@PreAuthorize("@ss.hasPermi('managment:organizetion:add')")
    @PostMapping
    public AjaxResult addInspectionOfficeAndGroup(){
        List<InspectionInfoVO> inspectionInfoVOList = iLeadingGroupFoundService.queryLeadingInspectionInfo();
        return AjaxResult.success(inspectionInfoVOList);
    }*/
}
