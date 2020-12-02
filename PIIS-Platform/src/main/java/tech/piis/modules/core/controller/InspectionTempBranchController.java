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
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionTempBranchPO;
import tech.piis.modules.core.service.IInspectionTempBranchService;
import tech.piis.modules.core.service.IPiisDocumentService;

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
     * 查询临时支部 列表
     *
     * @param inspectionTempBranch
     */
    @PreAuthorize("@ss.hasPermi('piis:branch:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionTempBranchPO inspectionTempBranch) throws BaseException {
        startPage();
        List<InspectionTempBranchPO> data = inspectionTempBranchService.selectInspectionTempBranchList(inspectionTempBranch);
        inspectionTempBranchCovert2List(data);
        return getDataTable(data);
    }

    /**
     * 新增临时支部
     *
     * @param inspectionTempBranch
     */
    @PreAuthorize("@ss.hasPermi('piis:branch:add')")
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
    @PreAuthorize("@ss.hasPermi('piis:branch:edit')")
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
    @PreAuthorize("@ss.hasPermi('piis:branch:remove')")
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
            inspectionTempBranch.setBranchMemberIds(paramsCovert2String(inspectionTempBranch.getMemberList()).get(0));
            inspectionTempBranch.setBranchMemberNames(paramsCovert2String(inspectionTempBranch.getMemberList()).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionTempBranchList
     */
    private void inspectionTempBranchCovert2List(List<InspectionTempBranchPO> inspectionTempBranchList) {
        if (!CollectionUtils.isEmpty(inspectionTempBranchList)) {
            inspectionTempBranchList.forEach(inspectionTempBranchPO -> inspectionTempBranchPO.setMemberList(paramsCovert2List(inspectionTempBranchPO.getBranchMemberIds(), inspectionTempBranchPO.getBranchMemberNames())));
        }
    }
}
