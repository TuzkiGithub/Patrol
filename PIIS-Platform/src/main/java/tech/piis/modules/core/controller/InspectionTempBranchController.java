package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.InspectionTempBranchMemberPO;
import tech.piis.modules.core.domain.po.InspectionTempBranchPO;
import tech.piis.modules.core.domain.vo.UnitsBriefVO;
import tech.piis.modules.core.domain.vo.UserBriefVO;
import tech.piis.modules.core.service.IInspectionTempBranchService;

import javax.validation.Valid;
import java.util.List;


/**
 * 临时支部 Controller
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/piis/branch")
public class InspectionTempBranchController extends BaseController {
    @Autowired
    private IInspectionTempBranchService inspectionTempBranchService;

    /**
     * 查询临时支部
     *
     * @param inspectionTempBranch
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @GetMapping("/info")
    public AjaxResult select(InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        InspectionTempBranchPO result = inspectionTempBranchService.selectInspectionTempBranchList(inspectionTempBranch);
        inspectionTempBranchCovert2List(result);
        return AjaxResult.success(result);
    }

    /**
     * 新增临时支部
     *
     * @param inspectionTempBranch
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "临时支部 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionTempBranchPO inspectionTempBranch) {
        if (null == inspectionTempBranch) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionTempBranchCovert2String(inspectionTempBranch);
        BizUtils.setCreatedOperation(InspectionTempBranchPO.class, inspectionTempBranch);
        return toAjax(inspectionTempBranchService.save(inspectionTempBranch));
    }

    /**
     * 修改临时支部
     *
     * @param inspectionTempBranch
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "临时支部 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        if (null == inspectionTempBranch) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionTempBranchCovert2String(inspectionTempBranch);
        BizUtils.setUpdatedOperation(InspectionTempBranchPO.class, inspectionTempBranch);
        return toAjax(inspectionTempBranchService.update(inspectionTempBranch));
    }

    /**
     * 删除临时支部
     * tempBranchIds 临时支部 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:workPreparation:perms')")
    @Log(title = "临时支部 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tempBranchIds}")
    public AjaxResult remove(@PathVariable Long[] tempBranchIds) throws BaseException {
        return toAjax(inspectionTempBranchService.deleteByInspectionTempBranchIds(tempBranchIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionTempBranch
     */
    private void inspectionTempBranchCovert2String(InspectionTempBranchPO inspectionTempBranch) {
        if (null != inspectionTempBranch) {
            List<InspectionTempBranchMemberPO> tempBranchMemberList = inspectionTempBranch.getTempBranchMemberList();
            if (!CollectionUtils.isEmpty(tempBranchMemberList)) {
                tempBranchMemberList.forEach(tempBranchMember -> {
                    tempBranchMember.setBranchMemberIds(paramsCovert2String(tempBranchMember.getMemberList()).get(0));
                    tempBranchMember.setBranchMemberNames(paramsCovert2String(tempBranchMember.getMemberList()).get(1));
                    tempBranchMember.setMemberList(paramsCovert2List(tempBranchMember.getBranchMemberIds(), tempBranchMember.getBranchMemberNames()));
                });
            }
            List<UserBriefVO> forwardSendList = inspectionTempBranch.getForwardSendList();
            List<UnitsBriefVO> orgList = inspectionTempBranch.getSignOrgList();
            inspectionTempBranch.setForwardSendIds(BizUtils.paramsCovert2String(forwardSendList).get(0));
            inspectionTempBranch.setForwardSendNames(BizUtils.paramsCovert2String(forwardSendList).get(1));
            inspectionTempBranch.setSignDeptIds(BizUtils.paramsCovert2OrgString(orgList).get(0));
            inspectionTempBranch.setSignDeptNames(BizUtils.paramsCovert2OrgString(orgList).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionTempBranch
     */
    private void inspectionTempBranchCovert2List(InspectionTempBranchPO inspectionTempBranch) {
        if (null != inspectionTempBranch) {
            List<InspectionTempBranchMemberPO> tempBranchMemberList = inspectionTempBranch.getTempBranchMemberList();
            if (!CollectionUtils.isEmpty(tempBranchMemberList)) {
                tempBranchMemberList.forEach(tempBranchMember -> tempBranchMember.setMemberList(paramsCovert2List(tempBranchMember.getBranchMemberIds(), tempBranchMember.getBranchMemberNames())));
            }
            inspectionTempBranch.setForwardSendList(BizUtils.paramsCovert2List(inspectionTempBranch.getForwardSendIds(), inspectionTempBranch.getForwardSendNames()));
            inspectionTempBranch.setSignOrgList(BizUtils.paramsCovert2OrgList(inspectionTempBranch.getSignDeptIds(), inspectionTempBranch.getSignDeptNames()));
        }
    }

}
