package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionTalkOutlinePO;
import tech.piis.modules.core.service.IInspectionTalkOutlineService;

import javax.validation.Valid;
import java.util.List;


/**
 * 谈话提纲Controller
 *
 * @author Tuzki
 * @date 2020-11-04
 */
@RestController
@RequestMapping("/piis/outline")
public class InspectionTalkOutlineController extends BaseController {
    @Autowired
    private IInspectionTalkOutlineService inspectionTalkOutlineService;

    /**
     * 查询谈话提纲列表
     *
     * @param inspectionTalkOutline
     */
    @PreAuthorize("@ss.hasPermi('piis:outline:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionTalkOutlinePO inspectionTalkOutline) throws BaseException {
        List<InspectionTalkOutlinePO> data = inspectionTalkOutlineService.selectInspectionTalkOutlineList(inspectionTalkOutline);
        return getDataTable(data);
    }

    /**
     * 批量修改谈话提纲
     *
     * @param inspectionTalkOutlineList
     */
    @PreAuthorize("@ss.hasPermi('piis:outline:edit')")
    @Log(title = "谈话提纲", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody @Valid List<InspectionTalkOutlinePO> inspectionTalkOutlineList) throws BaseException {
        if (!CollectionUtils.isEmpty(inspectionTalkOutlineList)) {
            inspectionTalkOutlineList.forEach(inspectionTalkOutline -> inspectionTalkOutlineService.update(inspectionTalkOutline));
        }
        return AjaxResult.success();
    }

}
