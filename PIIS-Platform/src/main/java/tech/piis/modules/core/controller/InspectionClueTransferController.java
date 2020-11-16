package tech.piis.modules.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.constant.GenConstants;
import tech.piis.common.enums.ResultEnum;
import tech.piis.common.enums.FileEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.core.domain.po.InspectionClueTransferDetailPO;
import tech.piis.modules.core.domain.po.InspectionClueTransferPO;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IInspectionClueTransferService;

import javax.validation.Valid;
import java.util.List;


/**
 * 线索移交 Controller
 *
 * @author Tuzki
 * @date 2020-10-27
 */
@RestController
@RequestMapping("/piis/clue/transfer")
public class InspectionClueTransferController extends BaseController {
    @Autowired
    private IInspectionClueTransferService inspectionClueTransferService;


    /**
     * 与前端接口定义文件类型
     */
    private static final Long TEMP_APPROVAL_FILE = 1L;
    private static final Long TEMP_HANDOVER_FILE = 2L;


    /**
     * 查询线索移交 列表
     *
     * @param inspectionClueTransfer
     */
    @PreAuthorize("@ss.hasPermi('piis:clue/transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        if (null != inspectionClueTransfer) {
            if (null == inspectionClueTransfer.getPageNum() || null == inspectionClueTransfer.getPageNum()) {
                inspectionClueTransfer.setPageNum(GenConstants.DEFAULT_PAGE_NUM);
                inspectionClueTransfer.setPageSize(GenConstants.DEFAULT_PAGE_SIZE);
            }
            inspectionClueTransfer.setPageNum(inspectionClueTransfer.getPageNum() * inspectionClueTransfer.getPageSize());
        }
        List<InspectionClueTransferPO> data = inspectionClueTransferService.selectInspectionClueTransferList(inspectionClueTransfer);
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(clueTransfer -> {
                List<InspectionClueTransferDetailPO> clueTransferDetailList = clueTransfer.getClueTransferDetailList();
                if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
                    clueTransferDetailList.forEach(clueTransferDetail -> convertTempDict(clueTransferDetail.getDocuments()));
                }
            });
        }
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(inspectionClueTransferService.count(inspectionClueTransfer));
    }

    /**
     * 查询线索移交 总览列表
     *
     * @param planId 巡视计划ID
     */
    @PreAuthorize("@ss.hasPermi('piis:clue/transfer:query')")
    @GetMapping("/count")
    public AjaxResult countInspectionClueTransferList(String planId) throws BaseException {
        return AjaxResult.success(inspectionClueTransferService.selectInspectionClueTransferCount(planId));
    }

    /**
     * 新增线索移交
     *
     * @param inspectionClueTransfer
     */
    @PreAuthorize("@ss.hasPermi('piis:clue/transfer:add')")
    @Log(title = "线索移交 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid InspectionClueTransferPO inspectionClueTransfer) {
        if (null == inspectionClueTransfer) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionClueTransfer);
        BizUtils.setCreatedOperation(InspectionClueTransferPO.class, inspectionClueTransfer);
        return toAjax(inspectionClueTransferService.save(inspectionClueTransfer));
    }

    /**
     * 修改线索移交
     *
     * @param inspectionClueTransfer
     */
    @PreAuthorize("@ss.hasPermi('piis:clue/transfer:edit')")
    @Log(title = "线索移交 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InspectionClueTransferPO inspectionClueTransfer) throws BaseException {
        if (null == inspectionClueTransfer) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        convertFileDict(inspectionClueTransfer);
        BizUtils.setUpdatedOperation(InspectionClueTransferPO.class, inspectionClueTransfer);
        return toAjax(inspectionClueTransferService.update(inspectionClueTransfer));
    }

    /**
     * 删除线索移交
     * clueTransferIds 线索移交 ID数组
     */
    @PreAuthorize("@ss.hasPermi('piis:clue/transfer:remove')")
    @Log(title = "线索移交 ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clueTransferIds}")
    public AjaxResult remove(@PathVariable String[] clueTransferIds) throws BaseException {
        return toAjax(inspectionClueTransferService.deleteByInspectionClueTransferIds(clueTransferIds));
    }

    /**
     * 临时文件字典 -》 实际文件字典
     *
     * @param inspectionClueTransfer
     */
    private void convertFileDict(InspectionClueTransferPO inspectionClueTransfer) {
        List<InspectionClueTransferDetailPO> clueTransferDetailList = inspectionClueTransfer.getClueTransferDetailList();
        if (!CollectionUtils.isEmpty(clueTransferDetailList)) {
            clueTransferDetailList.forEach(var -> {
                List<PiisDocumentPO> documents = var.getDocuments();
                if (!CollectionUtils.isEmpty(documents)) {
                    documents.forEach(document -> {
                        Long tempDictId = document.getFileDictId();
                        if (TEMP_APPROVAL_FILE.equals(tempDictId)) {
                            document.setFileDictId(FileEnum.APPROVAL_FILE.getCode());
                        } else if (TEMP_HANDOVER_FILE.equals(tempDictId)) {
                            document.setFileDictId(FileEnum.HANDOVER_FILE.getCode());
                        }
                    });
                }
            });
        }
    }

    /**
     * 实际文件字典 -》 临时文件字典
     *
     * @param documents
     */
    private void convertTempDict(List<PiisDocumentPO> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            documents.forEach(document -> {
                Long DictId = document.getFileDictId();
                if (FileEnum.APPROVAL_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_APPROVAL_FILE);
                } else if (FileEnum.HANDOVER_FILE.getCode().equals(DictId)) {
                    document.setFileDictId(TEMP_HANDOVER_FILE);
                }
            });
        }
    }
}
