package tech.piis.modules.managment.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.config.PIISConfig;
import tech.piis.framework.utils.file.FileUploadUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.managment.domain.po.MemberResumePO;
import tech.piis.modules.managment.domain.vo.GroupMemberVO;
import tech.piis.modules.managment.domain.vo.OrgManagmentVO;
import tech.piis.modules.managment.domain.vo.SysDeptVO;
import tech.piis.modules.managment.service.IOrganizationService;

import javax.validation.Valid;
import java.io.IOException;
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

    @Value("${piis.serverAddr}")
    private String serverFileUrl;

    /**
     * 根据机构编号ID查询直属下级机构
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @GetMapping("/find")
    public AjaxResult selectChildrenDeptByParentId(@RequestParam(value = "orgId", required = true) String orgId) throws BaseException {
        List<SysDeptVO> sysDeptList = iOrganizationService.selectChildrenDeptByParentId(orgId);
        return AjaxResult.success(sysDeptList);
    }

    /**
     * 根据子公司巡察机构成立信息以及人员信息
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @GetMapping("/sonLeadingGroup")
    public AjaxResult selectSonLeadingGroupInfo(@RequestParam(value = "orgId", required = true) String orgId) throws BaseException {
        GroupMemberVO groupMemberVO = iOrganizationService.selectSonLeadingGroupInfo(orgId);
        return AjaxResult.success(groupMemberVO);
    }

    /**
     * 查询机构信息列表
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(value = "orgName", required = false) String orgName) throws BaseException {
        return AjaxResult.success(iOrganizationService.selectListByOrgName(orgName));
    }

    /**
     * 新增机构
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:add')")
    @Log(title = "机构管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody OrgManagmentVO orgManagmentVO) throws BaseException {
        if (null == orgManagmentVO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(iOrganizationService.save(orgManagmentVO));
    }

    /**
     * 查询人员基本情况(家庭基本情况、专业培训情况、巡视巡察参与情况)
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @GetMapping("/memberInfo")
    public AjaxResult selectBasicInfoByMemberId(@RequestParam(value = "memberId", required = true) String memberId) throws BaseException {
        if (StringUtils.isBlank(memberId)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        Map<String, Object> resultMap = iOrganizationService.selectBasicInfoByMemberId(memberId);
        return AjaxResult.success(resultMap);
    }


    /**
     * 查询人员基本情况(家庭基本情况、专业培训情况、巡视巡察参与情况)
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @GetMapping("/memberResume")
    public AjaxResult selectMemberResumeByMemberId(@RequestParam(value = "memberId", required = true) String memberId) throws BaseException {
        if (StringUtils.isBlank(memberId)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        MemberResumePO memberResumePO = iOrganizationService.selectMemberResumeByMemberId(memberId);
        return AjaxResult.success(memberResumePO);
    }


    /**
     * 根据机构编号查询机构巡察办成立信息、巡察办成员信息、巡察领导小组成立信息、巡察领导小组成员信息、所属子公司相关信息
     * 、人员履历信息
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:query')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @GetMapping
    public AjaxResult selectWholeInfoByOrgId(@RequestParam(value = "orgId", required = true) String orgId) throws BaseException {
        if (StringUtils.isBlank(orgId)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        OrgManagmentVO orgManagmentVO = iOrganizationService.selectWholeInfoByOrgId(orgId);
        return AjaxResult.success(orgManagmentVO);
    }


    /**
     * 修改机构
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:edit')")
    @Log(title = "机构管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Valid @RequestBody OrgManagmentVO orgManagmentVO) throws BaseException {
        if (null == orgManagmentVO) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(iOrganizationService.update(orgManagmentVO));
    }

    /**
     * 删除机构
     *
     * @param orgId
     * @return
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:remove')")
    @Log(title = "机构管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable(value = "id") String orgId) throws BaseException {
        if (StringUtils.isBlank(orgId)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        return toAjax(this.iOrganizationService.delOrganById(orgId));
    }

    /**
     * 人员履历照片上传
     */
    @PreAuthorize("@ss.hasPermi('managment:organizetion:add')")
    @Log(title = "机构管理", businessType = BusinessType.OTHER)
    @PostMapping("/photo")
    public AjaxResult photoUpload(@RequestParam("photoFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = FileUploadUtils.upload(PIISConfig.getPhotoPath(), file);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("photo", serverFileUrl + "/upload/photo" + fileName);
            return ajax;
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }
}
