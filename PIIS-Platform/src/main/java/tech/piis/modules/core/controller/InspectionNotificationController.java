package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionNotificationPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionNotificationService;
import tech.piis.modules.core.service.IPiisDocumentService;

import javax.validation.Valid;
import java.util.List;


/**
 * 情况通报 Controller
 *
 * @author Tuzki
 * @date 2020-11-23
 */
@RestController
@RequestMapping("/piis/notification")
public class InspectionNotificationController extends BaseController {
    @Autowired
    private IInspectionNotificationService inspectionNotificationService;

    /**
     * 与前端定义的文件类型
     */
    private static final Long TEMP_FILE1 = 1L;
    private static final Long TEMP_FILE2 = 2L;

    /**
     * 查询情况通报 列表
     *
     * @param inspectionNotification
     */
    @PreAuthorize("@ss.hasPermi('piis:notification:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionNotificationPO inspectionNotification) throws BaseException {
        startPage();
        List<InspectionNotificationPO> data = inspectionNotificationService.selectInspectionNotificationList(inspectionNotification);
        convertTempDict(data);
        return getDataTable(data);
    }

    /**
     * 新增情况通报
     *
     * @param inspectionNotification
     */
    @PreAuthorize("@ss.hasPermi('piis:notification:add')")
    @Log(title = "情况通报 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionNotificationPO inspectionNotification) {
        if (null == inspectionNotification) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionNotification);
        BizUtils.setCreatedOperation(InspectionNotificationPO.class, inspectionNotification);
        return toAjax(inspectionNotificationService.save(inspectionNotification));
    }

    /**
     * 修改情况通报
     *
     * @param inspectionNotification
     */
    @PreAuthorize("@ss.hasPermi('piis:notification:edit')")
    @Log(title = "情况通报 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionNotificationPO inspectionNotification) throws BaseException {
        if (null == inspectionNotification) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionNotification);
        BizUtils.setUpdatedOperation(InspectionNotificationPO.class, inspectionNotification);
        return toAjax(inspectionNotificationService.update(inspectionNotification));
    }

    /**
     * 删除情况通报
     * notificationIds 情况通报 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:notification:remove')")
    @Log(title = "情况通报 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{notificationIds}")
    public AjaxResult remove(@PathVariable String[] notificationIds) throws BaseException {
        return toAjax(inspectionNotificationService.deleteByInspectionNotificationIds(notificationIds));
    }

    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionNotification
     */
    private void convertFileDict(InspectionNotificationPO inspectionNotification) {
        List<PiisDocumentPO> documents = inspectionNotification.getDocuments();
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long tempDictId = document.getFileDictId();
                if (TEMP_FILE1.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.TEACHING_FILE.getCode());
                } else if (TEMP_FILE2.equals(tempDictId)) {
                    document.setFileDictId(FileEnum.INVITATION_FILE.getCode());
                }
            });
        }
    }


    /**
     * 实际文件字典 -》 临时文件字典
     *
     * @param notificationList
     */
    private void convertTempDict(List<InspectionNotificationPO> notificationList) {
        if (!CollectionUtils.isEmpty(notificationList)) {
            notificationList.forEach(notification -> {
                List<PiisDocumentPO> documents = notification.getDocuments();
                if (!CollectionUtils.isEmpty(documents)) {
                    documents.forEach(document -> {
                        Long DictId = document.getFileDictId();
                        if (FileEnum.TEACHING_FILE.getCode().equals(DictId)) {
                            document.setFileDictId(TEMP_FILE1);
                        } else if (FileEnum.INVITATION_FILE.getCode().equals(DictId)) {
                            document.setFileDictId(TEMP_FILE2);
                        }

                    });
                }
            });
        }


    }
}
