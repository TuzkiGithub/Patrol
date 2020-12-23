package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ApprovalEnum;
import tech.piis.common.enums.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionFeedbackPO;
import tech.piis.modules.core.domain.vo.UnitsBriefVO;
import tech.piis.modules.core.service.IInspectionFeedbackService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static tech.piis.common.constant.PiisConstants.NO_APPROVAL;


/**
 * 反馈意见 Controller
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/piis/feedback")
public class InspectionFeedbackController extends BaseController {
    @Autowired
    private IInspectionFeedbackService inspectionFeedbackService;

    /**
     * 查询反馈意见 列表
     *
     * @param inspectionFeedback
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionFeedbackPO inspectionFeedback) throws BaseException {
        if (null == inspectionFeedback) {
            return new TableDataInfo()
                    .setCode(ResultEnum.FAILED.getCode())
                    .setMsg(ResultEnum.FAILED.getMsg());
        }
        List<InspectionFeedbackPO> data = inspectionFeedbackService.selectInspectionFeedbackList(inspectionFeedback);
        inspectionFeedbackCovert2List(data);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(inspectionFeedbackService.count(inspectionFeedback));
    }

    /**
     * 查询反馈意见 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @GetMapping("/count")
    public AjaxResult countInspectionFeedbackList(String planId) throws BaseException {
        return AjaxResult.success(inspectionFeedbackService.selectInspectionFeedbackCount(planId));
    }

    /**
     * 新增反馈意见
     *
     * @param inspectionFeedback
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈意见 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionFeedbackPO inspectionFeedback) {
        if (null == inspectionFeedback) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionFeedback.getIsApproval()) {
            inspectionFeedback.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionFeedback.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        inspectionFeedbackCovert2String(inspectionFeedback);
        BizUtils.setCreatedOperation(InspectionFeedbackPO.class, inspectionFeedback);
        return toAjax(inspectionFeedbackService.save(inspectionFeedback));
    }

    /**
     * 审批反馈意见
     *
     * @param inspectionFeedbackList
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈意见 ", businessType = BusinessType.APPROVAL)
    @PostMapping("approval")
    public AjaxResult approval(@RequestBody @Valid List<InspectionFeedbackPO> inspectionFeedbackList) {
        if (CollectionUtils.isEmpty(inspectionFeedbackList)) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionFeedbackService.doApprovals(inspectionFeedbackList);
        return AjaxResult.success();
    }


    /**
     * 修改反馈意见
     *
     * @param inspectionFeedback
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈意见 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionFeedbackPO inspectionFeedback) throws BaseException {
        if (null == inspectionFeedback) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        if (NO_APPROVAL == inspectionFeedback.getIsApproval()) {
            inspectionFeedback.setApprovalFlag(ApprovalEnum.NO_APPROVAL.getCode());
        } else {
            inspectionFeedback.setApprovalFlag(ApprovalEnum.TO_BE_SUBMIT.getCode());
        }
        inspectionFeedbackCovert2String(inspectionFeedback);
        BizUtils.setUpdatedOperation(InspectionFeedbackPO.class, inspectionFeedback);
        return toAjax(inspectionFeedbackService.update(inspectionFeedback));
    }

    /**
     * 删除反馈意见
     * feedbackIds 反馈意见 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:feedback:perms')")
    @Log(title = "反馈意见 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable String[] feedbackIds) throws BaseException {
        return toAjax(inspectionFeedbackService.deleteByInspectionFeedbackIds(feedbackIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionFeedback
     */
    private void inspectionFeedbackCovert2String(InspectionFeedbackPO inspectionFeedback) {
        if (null != inspectionFeedback) {
            List<UnitsBriefVO> unitsBriefList = inspectionFeedback.getCopyUnitsList();
            StringBuilder copyCompanyId = new StringBuilder();
            StringBuilder copyCompanyName = new StringBuilder();
            if (!CollectionUtils.isEmpty(unitsBriefList)) {
                for (UnitsBriefVO unitsBriefVO : unitsBriefList) {
                    copyCompanyId.append(unitsBriefVO.getUnitsId()).append(",");
                    copyCompanyName.append(unitsBriefVO.getUnitsName()).append(",");
                }
                inspectionFeedback.setCopyCompanyId(copyCompanyId.toString().substring(0, copyCompanyId.toString().lastIndexOf(",")));
                inspectionFeedback.setCopyCompanyName(copyCompanyName.toString().substring(0, copyCompanyName.toString().lastIndexOf(",")));
            }

        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionFeedbackList
     */
    private void inspectionFeedbackCovert2List(List<InspectionFeedbackPO> inspectionFeedbackList) {
        if (!CollectionUtils.isEmpty(inspectionFeedbackList)) {
            inspectionFeedbackList.forEach(feedback -> {
                String copyCompanyId = feedback.getCopyCompanyId();
                String copyCompanyName = feedback.getCopyCompanyName();
                String[] copyCompanyIdArr = copyCompanyId.split(",");
                String[] copyCompanyNameArr = copyCompanyName.split(",");
                List<UnitsBriefVO> unitsBriefList = new ArrayList<>();
                for (int i = 0; i < copyCompanyIdArr.length; i++) {
                    UnitsBriefVO unitsBriefVO = new UnitsBriefVO();
                    unitsBriefVO.setUnitsId(copyCompanyIdArr[i]);
                    unitsBriefVO.setUnitsName(copyCompanyNameArr[i]);
                    unitsBriefList.add(unitsBriefVO);
                }
                feedback.setCopyUnitsList(unitsBriefList);

            });

        }
    }
}
