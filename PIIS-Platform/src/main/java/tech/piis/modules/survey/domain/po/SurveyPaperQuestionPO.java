package tech.piis.modules.survey.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * 试卷题目关系对象 survey_paper_question
 *
 * @author Tuzki
 * @date 2020-11-09
 */

@TableName("survey_paper_question")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SurveyPaperQuestionPO extends PIBaseEntity {
    /**
     * 试卷题目关系ID
     */
    private Long paperQuestionId;
    /**
     * 试卷ID
     */
    private String paperId;
    /**
     * 题目ID
     */
    private String questionId;

}
