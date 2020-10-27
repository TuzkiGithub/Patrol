package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.GenConstants;
import tech.piis.common.constant.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionConsultInfoPO;
import tech.piis.modules.core.service.IInspectionConsultInfoService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.List;


/**
 * 查阅资料Controller
 *
 * @author Kevin
 * @date 2020-10-19
 */
@RestController
@RequestMapping("/piis/consult/info")
public class InspectionConsultInfoController extends BaseController {
    @Autowired
    private IInspectionConsultInfoService inspectionConsultInfoService;

    @Autowired
    private IPiisDocumentService documentService;

    /**
     * 查询查阅资料列表
     *
     * @param inspectionConsultInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        Long unitsId = null;
        if (null != inspectionConsultInfo) {
            unitsId = Long.valueOf(inspectionConsultInfo.getUnitsId());
            if (null == inspectionConsultInfo.getPageNum() || null == inspectionConsultInfo.getPageNum()) {
                inspectionConsultInfo.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionConsultInfo.setPageSize(GenConstants.DEFAULT_PAGE_SIZE);
            }
            inspectionConsultInfo.setPageNum(inspectionConsultInfo.getPageNum() * inspectionConsultInfo.getPageSize());
        }
        List<InspectionConsultInfoPO> data = inspectionConsultInfoService.selectInspectionConsultInfoList(inspectionConsultInfo);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(inspectionConsultInfoService.count(unitsId));
    }

    /**
     * 查询查阅资料文件
     *
     * @param consultInfoId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:query')")
    @GetMapping("/file")
    public AjaxResult findInspectionConsultInfoFile(String consultInfoId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("ConsultInfo" + consultInfoId));
    }

    /**
     * 查询查阅资料详情文件
     *
     * @param consultInfoDetailId 文件关联ID
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:query')")
    @GetMapping("/detail/file")
    public AjaxResult findInspectionConsultInfoDetailFile(String consultInfoDetailId) throws BaseException {
        return AjaxResult.success(documentService.getFileListByBizId("ConsultInfoDetail" + consultInfoDetailId));
    }

    /**
     * 查询查阅资料总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionConsultInfoList(String planId) throws BaseException {
        return AjaxResult.success(inspectionConsultInfoService.selectInspectionConsultInfoCount(planId));
    }

    /**
     * 新增查阅资料
     *
     * @param inspectionConsultInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:add')")
    @Log(title = "查阅资料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionConsultInfoPO inspectionConsultInfo) {
        if (null == inspectionConsultInfo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(InspectionConsultInfoPO.class, inspectionConsultInfo);
        return toAjax(inspectionConsultInfoService.save(inspectionConsultInfo));
    }

    /**
     * 修改查阅资料
     *
     * @param inspectionConsultInfo
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:edit')")
    @Log(title = "查阅资料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionConsultInfoPO inspectionConsultInfo) throws BaseException {
        if (null == inspectionConsultInfo) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(InspectionConsultInfoPO.class, inspectionConsultInfo);
        return toAjax(inspectionConsultInfoService.update(inspectionConsultInfo));
    }

    /**
     * 删除查阅资料
     * consultInfoIds 查阅资料ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:consult/info:remove')")
    @Log(title = "查阅资料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{consultInfoIds}")
    public AjaxResult remove(@PathVariable String[] consultInfoIds) throws BaseException {
        return toAjax(inspectionConsultInfoService.deleteByInspectionConsultInfoIds(consultInfoIds));
    }
}
