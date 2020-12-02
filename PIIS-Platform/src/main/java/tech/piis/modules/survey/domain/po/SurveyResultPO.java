package tech.piis.modules.survey.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 试卷/问卷回答情况对象 survey_result
 *
 * @author Tuzki
 * @date 2020-11-18
 */

@TableName("survey_result")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SurveyResultPO extends PIBaseEntity {
    /**
     * 回答结果ID
     */
    @TableId(value = "RESULT_ID", type = IdType.AUTO)
    private Long resultId;
    /**
     * 用户ID
     */
    @NotBlank(message = "用户编号不能为空！")
    private String userId;
    /**
     * 试卷问题关系ID
     */
    @NotNull(message = "题目试卷关系编号不能为空！")
    private Long paperQuestionId;
    /**
     * 用户答案
     */
    private String answer;

    /**
     * 是否为标准答案，仅限单选，多选，判断
     */
    private Boolean answerFlag;

    /**
     * 选项和A，B，C，D映射关系
     */
    private String optionMapping;

    /**
     * 试卷/问卷
     */
    private SurveyPaperPO paper;
}