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
import tech.piis.modules.core.domain.po.InspectionIndividualTalkPO;
import tech.piis.modules.core.domain.vo.UserBriefVO;
import tech.piis.modules.core.service.IInspectionIndividualTalkService;

import javax.validation.Valid;
import java.util.List;


/**
 * 个别谈话 Controller
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/piis/talk")
public class InspectionIndividualTalkController extends BaseController {
    @Autowired
    private IInspectionIndividualTalkService inspectionIndividualTalkService;


    /**
     * 查询个别谈话 列表
     *
     * @param inspectionIndividualTalk
     */
    @PreAuthorize("@ss.hasPermi('piis:talk:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException {
        startPage();
        List<InspectionIndividualTalkPO> data = inspectionIndividualTalkService.selectInspectionIndividualTalkList(inspectionIndividualTalk);
        inspectionIndividualTalkCovert2List(data);
        return getDataTable(data);
    }

    /**
     * 查询个别谈话 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:talk:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionIndividualTalkList(String planId) throws BaseException {
        return AjaxResult.success(inspectionIndividualTalkService.selectInspectionIndividualTalkCount(planId));
    }

    /**
     * 新增个别谈话
     *
     * @param inspectionIndividualTalk
     */
    @PreAuthorize("@ss.hasPermi('piis:talk:add')")
    @Log(title = "个别谈话 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionIndividualTalkPO inspectionIndividualTalk) {
        if (null == inspectionIndividualTalk) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionIndividualTalkCovert2String(inspectionIndividualTalk);
        BizUtils.setCreatedOperation(InspectionIndividualTalkPO.class, inspectionIndividualTalk);
        return toAjax(inspectionIndividualTalkService.save(inspectionIndividualTalk));
    }

    /**
     * 修改个别谈话
     *
     * @param inspectionIndividualTalk
     */
    @PreAuthorize("@ss.hasPermi('piis:talk:edit')")
    @Log(title = "个别谈话 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionIndividualTalkPO inspectionIndividualTalk) throws BaseException {
        if (null == inspectionIndividualTalk) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        inspectionIndividualTalkCovert2String(inspectionIndividualTalk);
        BizUtils.setUpdatedOperation(InspectionIndividualTalkPO.class, inspectionIndividualTalk);
        return toAjax(inspectionIndividualTalkService.update(inspectionIndividualTalk));
    }

    /**
     * 删除个别谈话
     * individualTalkIds 个别谈话 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:talk:remove')")
    @Log(title = "个别谈话 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{individualTalkIds}")
    public AjaxResult remove(@PathVariable Long[] individualTalkIds) throws BaseException {
        return toAjax(inspectionIndividualTalkService.deleteByInspectionIndividualTalkIds(individualTalkIds));
    }

    /**
     * 参数类型转换
     *
     * @param inspectionIndividualTalk
     */
    private void inspectionIndividualTalkCovert2String(InspectionIndividualTalkPO inspectionIndividualTalk) {
        if (null != inspectionIndividualTalk) {
            List<UserBriefVO> talkPersonList = inspectionIndividualTalk.getTalkPersonList();
            List<UserBriefVO> recordList = inspectionIndividualTalk.getRecordList();
            inspectionIndividualTalk.setTalkPersonId(paramsCovert2String(talkPersonList).get(0));
            inspectionIndividualTalk.setTalkPersonName(paramsCovert2String(talkPersonList).get(1));
            inspectionIndividualTalk.setRecordPersonId(paramsCovert2String(recordList).get(0));
            inspectionIndividualTalk.setRecordPersonName(paramsCovert2String(recordList).get(1));
        }
    }


    /**
     * 参数类型转换
     *
     * @param inspectionIndividualTalkList
     */
    private void inspectionIndividualTalkCovert2List(List<InspectionIndividualTalkPO> inspectionIndividualTalkList) {
        if (!CollectionUtils.isEmpty(inspectionIndividualTalkList)) {
            inspectionIndividualTalkList.forEach(var -> {
                var.setRecordList(paramsCovert2List(var.getRecordPersonId(), var.getRecordPersonName()));
                var.setTalkPersonList(paramsCovert2List(var.getTalkPersonId(), var.getTalkPersonName()));
            });
        }
    }
}
