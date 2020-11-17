package tech.piis.modules.survey.domain.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 测评/问卷对象 survey_paper
 *
 * @author Tuzki
 * @date 2020-11-09
 */

@TableName("survey_paper")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SurveyPaperPO extends PIBaseEntity {
    /**
     * 试卷ID
     */
    @TableId(value = "PAPER_ID")
    private String paperId;
    /**
     * 试卷名称
     */
    @Excel(name = "试卷名称", orderNum = "0", width = 25, needMerge = true)
    @NotBlank(message = "试卷名称不能为空！")
    private String paperName;
    /**
     * 0：试卷，1：调查问卷
     */
    @NotNull(message = "试卷名称不能为空！")
    private Integer paperType;

    /**
     * 试卷业务类型
     */
    @Excel(name = "试卷业务类型", orderNum = "1", replace = {"政务类_1", "监察类_2", "其他_3"}, width = 25, needMerge = true)
    @NotNull(message = "试卷业务类型不能为空！")
    private Integer paperBusinessType;
    /**
     * 答题开始时间
     */
    @Excel(name = "答题开始时间", orderNum = "2", width = 25, needMerge = true)
    private Date startDate;
    /**
     * 答题截至时间
     */
    @Excel(name = "答题截至时间", orderNum = "3", width = 25, needMerge = true)
    private Date endDate;

    /**
     * 题目列表
     */
    @NotEmpty(message = "题目列表不能为空！")
    private List<SurveyQuestionPO> questionList;

    /**
     * 单选题数量
     */
    @TableField(exist = false)
    private Integer singleCount;

    /**
     * 多选题数量
     */
    @TableField(exist = false)
    private Integer doubleCount;

    /**
     * 判断题数量
     */
    @TableField(exist = false)
    private Integer judgeCount;

    /**
     * 填空题数量
     */
    @TableField(exist = false)
    private Integer blankCount;

    /**
     * 简答题数量
     */
    @TableField(exist = false)
    private Integer qaCount;
}
