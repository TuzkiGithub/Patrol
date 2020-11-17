package tech.piis.modules.survey.domain.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 题目对象 survey_question
 *
 * @author Tuzki
 * @date 2020-11-09
 */

@TableName("survey_question")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SurveyQuestionPO extends PIBaseEntity {
    /**
     * 题目ID
     */
    @TableId(value = "QUESTION_ID")
    private String questionId;

    /**
     * 巡视计划ID
     */
    private String planId;

    /**
     * 题目内容
     */
    @Excel(name = "题目内容", orderNum = "0", width = 25, needMerge = true)
    @NotBlank(message = "题目内容不能为空！")
    private String questionName;
    /**
     * 题目类型（单选、多选、判断、填空、简答）
     */
    @Excel(name = "题目类型", orderNum = "1", replace = {"单选_1", "多选_2", "判断_3", "填空_4", "简答_5"}, suffix = "题", width = 25, needMerge = true)
    @NotNull(message = "题目类型不能为空！")
    private Integer questionType;

    /**
     * 题目分数
     */
    @Excel(name = "题目默认分数", orderNum = "2", width = 25, needMerge = true)
    private Integer score;
    /**
     * 是否必填
     */
    @Excel(name = "是否必填", orderNum = "3", replace = {"否_0", "是_1"}, width = 25, needMerge = true)
    @NotNull(message = "题目是否必填不能为空！")
    private Integer requiredFlag;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 题目类型
     * 1 测评题目
     * 2 问卷题目
     */
    private Integer type;

    /**
     * 参考答案
     */
    @Excel(name = "参考答案", orderNum = "5", width = 50, needMerge = true)
    private String referenceAnswer;

    /**
     * 适用范围公司ID
     */
    private String rangeId;

    /**
     * 适用范围公司名称
     */
    @Excel(name = "适用范围", orderNum = "4", width = 50, needMerge = true)
    private String rangeName;

    /**
     * 选项列表
     */
    @ExcelCollection(name = "选项", orderNum = "6")
    private List<SurveyOptionPO> optionList;

    public SurveyQuestionPO() {

    }
}
