package tech.piis.modules.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.exception.BaseException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.survey.domain.po.SurveyResultPO;
import tech.piis.modules.survey.service.ISurveyResultService;

import javax.validation.Valid;
import java.util.List;


/**
 * 试卷/问卷回答情况Controller
 *
 * @author Tuzki
 * @date 2020-11-18
 */
@RestController
@RequestMapping("/survey/result")
public class SurveyResultController extends BaseController {
    @Autowired
    private ISurveyResultService surveyResultService;

    /**
     * 查询试卷/问卷回答情况列表
     *
     * @param surveyResult
     */
    @PreAuthorize("@ss.hasPermi('survey:result:list')")
    @GetMapping("/list")
    public TableDataInfo list(SurveyResultPO surveyResult) throws BaseException {
        startPage();
        List<SurveyResultPO> data = surveyResultService.selectSurveyResultList(surveyResult);
        return getDataTable(data);
    }


    /**
     * 新增试卷/问卷回答情况
     *
     * @param surveyResult
     */
    @PreAuthorize("@ss.hasPermi('survey:result:add')")
    @Log(title = "试卷/问卷回答情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid SurveyResultPO surveyResult) {
        if (null == surveyResult) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(SurveyResultPO.class, surveyResult);
        return toAjax(surveyResultService.save(surveyResult));
    }

    /**
     * 修改试卷/问卷回答情况
     *
     * @param surveyResult
     */
    @PreAuthorize("@ss.hasPermi('survey:result:edit')")
    @Log(title = "试卷/问卷回答情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SurveyResultPO surveyResult) throws BaseException {
        if (null == surveyResult) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(SurveyResultPO.class, surveyResult);
        return toAjax(surveyResultService.update(surveyResult));
    }
}