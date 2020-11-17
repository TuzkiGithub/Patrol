package tech.piis.modules.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.survey.domain.po.SurveyPaperPO;
import tech.piis.modules.survey.service.ISurveyPaperService;

import javax.validation.Valid;
import java.util.List;

import static tech.piis.common.constant.BizConstants.PARAMS_NULL;


/**
 * 试卷/问卷Controller
 *
 * @author Tuzki
 * @date 2020-11-09
 */
@RestController
@RequestMapping("/survey/paper")
public class SurveyPaperController extends BaseController {
    @Autowired
    private ISurveyPaperService surveyPaperService;


    /**
     * 查询试卷/问卷列表
     *
     * @param surveyPaper
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:list')")
    @GetMapping("/list")
    public TableDataInfo list(SurveyPaperPO surveyPaper) throws BaseException {
        //校验参数
        if (null == surveyPaper) {
            return new TableDataInfo()
                    .setCode(ResultEnum.FAILED.getCode())
                    .setMsg(BizConstants.PARAMS_NULL);
        } else {
            if (null == surveyPaper.getPageNum() || null == surveyPaper.getPageSize()) {
                return new TableDataInfo()
                        .setCode(ResultEnum.FAILED.getCode())
                        .setMsg(BizConstants.PAGE_PARAMS_NULL);
            }
            if (null == surveyPaper.getPaperType()) {
                return new TableDataInfo()
                        .setCode(ResultEnum.FAILED.getCode())
                        .setMsg(BizConstants.PARAMS_NULL);
            }
        }
        surveyPaper.setPageNum(surveyPaper.getPageNum() * surveyPaper.getPageSize());
        List<SurveyPaperPO> data = surveyPaperService.selectSurveyPaperList(surveyPaper);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(surveyPaperService.count(surveyPaper));
    }

    /**
     * 新增试卷/问卷
     * PS:从题库中选择
     *
     * @param surveyPaper
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:add')")
    @Log(title = "试卷/问卷", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid SurveyPaperPO surveyPaper) {
        if (null == surveyPaper) {
            return AjaxResult.error(PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(SurveyPaperPO.class, surveyPaper);
        return toAjax(surveyPaperService.saveBySelect(surveyPaper));
    }


    /**
     * 新增试卷/问卷
     * PS:系统生成
     *
     * @param paperType 试卷类型 1：测评、2：问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:add')")
    @Log(title = "试卷/问卷", businessType = BusinessType.INSERT)
    @PostMapping("create")
    public AjaxResult add(Integer paperType) {
        return AjaxResult.success();
    }

    /**
     * 新增试卷/问卷
     * PS:通过文件导入
     *
     * @param paperType 试卷类型 1：测评、2：问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:add')")
    @Log(title = "试卷/问卷", businessType = BusinessType.INSERT)
    @PostMapping("import")
    public AjaxResult add(Integer paperType, MultipartFile file) {
        return AjaxResult.success();
    }


    /**
     * 导出试卷
     *
     * @param paperId 试卷ID
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:export')")
    @PostMapping("export")
    public AjaxResult export(String paperId) {
        return AjaxResult.success();
    }


    /**
     * 修改试卷/问卷
     *
     * @param surveyPaper
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:edit')")
    @Log(title = "试卷/问卷", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SurveyPaperPO surveyPaper) throws BaseException {
        if (null == surveyPaper) {
            return AjaxResult.error(PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(SurveyPaperPO.class, surveyPaper);
        return toAjax(surveyPaperService.update(surveyPaper));
    }

    /**
     * 删除试卷/问卷
     * paperIds 试卷/问卷ID数组
     */
    @PreAuthorize("@ss.hasPermi('survey:paper:remove')")
    @Log(title = "试卷/问卷", businessType = BusinessType.DELETE)
    @DeleteMapping("/{paperIds}")
    public AjaxResult remove(@PathVariable String[] paperIds) throws BaseException {
        return toAjax(surveyPaperService.deleteBySurveyPaperIds(paperIds));
    }
}
