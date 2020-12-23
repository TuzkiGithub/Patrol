package tech.piis.modules.person.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.piis.modules.core.domain.po.PIBaseEntity;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * ClassName : TrainingPracticePO
 * Package : tech.piis.modules.person.domain
 * Description : 以干代训
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("person_training_practice")
public class TrainingPracticePO extends PIBaseEntity {
    /**
     * 以干代训编号
     */
    @TableId(value = "practice_id",type = IdType.AUTO)
    private Long practiceId;
    /**
     * 机构编号
     */
    private String orgId;
    /**
     * 所归一级子机构编号
     */
    private String firstbranchId;
    /**
     * 以干代训年度
     */
    private String practiceYear;
    /**
     * 成员编号
     */
    private String memberId;
    /**
     * 成员名称
     */
    @NotBlank(message = "成员姓名不能为空")
    private String memberName;
    /**
     * 成员单位
     */
    @NotBlank(message = "所在单位不能为空")
    private String memberUnit;
    /**
     *  成员职务
     */
    @NotBlank(message = "所在单位职位不能为空")
    private String memberPost;
    /**
     * 参与类型 0 - 巡视 , 1 - 巡察
     */
    private String trainingType;
    /**
     * 组内职务
     */
    @NotBlank(message = "组内职位不能为空")
    private String innerPost ;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     *  结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 以干代训次数
     */
    @TableField(exist = false)
    private Long amount;
}
