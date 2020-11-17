package tech.piis.modules.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.piis.common.constant.BizConstants;
import tech.piis.common.enums.PaperTypeEnum;
import tech.piis.common.enums.ResultEnum;
import tech.piis.common.exception.BaseException;
import tech.piis.common.exception.file.QuestionFileException;
import tech.piis.framework.aspectj.lang.annotation.Log;
import tech.piis.framework.aspectj.lang.enums.BusinessType;
import tech.piis.framework.utils.BizUtils;
import tech.piis.framework.utils.file.FileUtils;
import tech.piis.framework.web.controller.BaseController;
import tech.piis.framework.web.domain.AjaxResult;
import tech.piis.framework.web.page.TableDataInfo;
import tech.piis.modules.survey.domain.po.SurveyQuestionPO;
import tech.piis.modules.survey.service.ISurveyQuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


/**
 * 题目Controller
 *
 * @author Tuzki
 * @date 2020-11-09
 */
@RestController
@RequestMapping("/survey/question")
public class SurveyQuestionController extends BaseController {
    @Autowired
    private ISurveyQuestionService surveyQuestionService;

    @Value("${piis.profile}")
    private String baseDir;

    /**
     * 查询题目列表
     *
     * @param surveyQuestion
     */
    @PreAuthorize("@ss.hasPermi('survey:question:list')")
    @GetMapping("/list")
    public TableDataInfo list(SurveyQuestionPO surveyQuestion) throws BaseException {
        if (null != surveyQuestion) {
            surveyQuestion.setPageNum(surveyQuestion.getPageNum() * surveyQuestion.getPageSize());
        }
        List<SurveyQuestionPO> data = surveyQuestionService.selectSurveyQuestionList(surveyQuestion);
        return new TableDataInfo()
                .setCode(ResultEnum.SUCCESS.getCode())
                .setMsg(ResultEnum.SUCCESS.getMsg())
                .setRows(data)
                .setTotal(surveyQuestionService.count(surveyQuestion));
    }

    /**
     * 新增题目
     *
     * @param surveyQuestion
     */
    @PreAuthorize("@ss.hasPermi('survey:question:add')")
    @Log(title = "题目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Valid SurveyQuestionPO surveyQuestion) {
        if (null == surveyQuestion) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setCreatedOperation(SurveyQuestionPO.class, surveyQuestion);
        return toAjax(surveyQuestionService.save(surveyQuestion));
    }

    /**
     * 修改题目
     *
     * @param surveyQuestion
     */
    @PreAuthorize("@ss.hasPermi('survey:question:edit')")
    @Log(title = "题目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SurveyQuestionPO surveyQuestion) throws BaseException {
        if (null == surveyQuestion) {
            return AjaxResult.error(BizConstants.PARAMS_NULL);
        }
        BizUtils.setUpdatedOperation(SurveyQuestionPO.class, surveyQuestion);
        return toAjax(surveyQuestionService.update(surveyQuestion));
    }

    /**
     * 删除题目
     * questionIds 题目ID数组
     */
    @PreAuthorize("@ss.hasPermi('survey:question:remove')")
    @Log(title = "题目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{questionIds}")
    public AjaxResult remove(@PathVariable String[] questionIds) throws BaseException {
        return toAjax(surveyQuestionService.deleteBySurveyQuestionIds(questionIds));
    }


    /**
     * 下载模板
     *
     * @param type     1测评 2问卷
     * @param response
     * @param request
     * @throws Exception
     */
    @PostMapping("template")
    public void downloadTemplate(Integer type, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, "题目导入模板.xlsx"));
        String downloadUrl;
        if (PaperTypeEnum.EVALUATION.getCode().equals(type)) {
            downloadUrl = baseDir + "/survey/template/测评题目导入模板.xlsx";
        } else {
            downloadUrl = baseDir + "/survey/template/问卷题目导入模板.xlsx";
        }
        FileUtils.writeBytes(downloadUrl, response.getOutputStream());
    }

    /**
     * 模板导入数据
     *
     * @param type 1测评 2问卷
     * @param file
     * @return
     * @throws QuestionFileException
     */
    @PostMapping("import")
    public AjaxResult importData(Integer type, MultipartFile file) throws QuestionFileException {
        if (null != file) {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            if (!fileName.contains("xlsx") || !fileName.contains("xls")) {
                return AjaxResult.error("文件格式错误！");
            }
        } else {
            return AjaxResult.error("文件不能为空！");
        }

        if (null == type) {
            return AjaxResult.error("题目类型不能为空！");
        }
        surveyQuestionService.importQuestion(file, type);
        return AjaxResult.success();
    }
}
