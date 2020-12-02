package tech.piis.modules.survey.domain.po;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * 选项对象 survey_option
 *
 * @author Tuzki
 * @date 2020-11-09
 */

@TableName("survey_option")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class SurveyOptionPO extends PIBaseEntity {
    /**
     * 选项ID
     */
    @TableId(value = "OPTION_ID", type = IdType.AUTO)
    private Long optionId;
    /**
     * 题目ID
     */
    private String questionId;
    /**
     * 选项名称
     */
    @Excel(name = "选项名称")
    private String optionName;

    /**
     * 备注
     */
    private String remark;

    public SurveyOptionPO() {

    }

}
