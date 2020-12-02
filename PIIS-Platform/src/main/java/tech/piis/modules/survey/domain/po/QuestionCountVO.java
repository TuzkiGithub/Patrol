package tech.piis.modules.survey.domain.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.survey.domain.po
 * User: Tuzki
 * Date: 2020/11/17
 * Time: 16:14
 * Description:
 */
@Data
@Accessors(chain = true)
public class QuestionCountVO {
    /**
     * 单选题数量
     */
    private Integer singleCount;

    /**
     * 多选题数量
     */
    private Integer doubleCount;

    /**
     * 判断题数量
     */
    private Integer judgeCount;

    /**
     * 填空题数量
     */
    private Integer blankCount;

    /**
     * 简答题数量
     */
    private Integer qaCount;
}
