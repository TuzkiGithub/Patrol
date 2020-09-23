package tech.piis.modules.core.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.modules.core.domain.po.PiisDocumentPO;
import tech.piis.modules.core.service.IPiisDocumentService;

/**
 * 巡视附件 Controller
 * 
 * @author Tuzki
 * @date 2020-09-14
 */
@RestController
@RequestMapping("/system/document")
public class PiisDocumentController extends BaseController
{
    @Autowired
    private IPiisDocumentService piisDocumentService;

    /**
     * 查询巡视附件 列表
     */
    @PreAuthorize("@ss.hasPermi('system:document:list')")
    @GetMapping("/list")
    public AjaxResult list(PiisDocumentPO piisDocumentPO) {
        return null;
    }


    /**
     * 新增巡视附件 
     */
    @PreAuthorize("@ss.hasPermi('system:document:add')")
    @Log(title = "巡视附件 ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PiisDocumentPO piisDocumentPO) {
        return toAjax(0);
    }

    /**
     * 修改巡视附件 
     */
    @PreAuthorize("@ss.hasPermi('system:document:edit')")
    @Log(title = "巡视附件 ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PiisDocumentPO piisDocumentPO) {
        return toAjax(0);
    }

    /**
     * 删除巡视附件 
     */
    @PreAuthorize("@ss.hasPermi('system:document:remove')")
    @Log(title = "巡视附件 ", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(0);
    }
}
