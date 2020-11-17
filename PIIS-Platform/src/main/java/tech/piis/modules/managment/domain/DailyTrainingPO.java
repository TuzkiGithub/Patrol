package tech.piis.modules.managment.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * ClassName : DailyTrainingPO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *  日常培训实体类
 * @author : chenhui@xvco.com
 */
@Data
@TableName("daily_training")
public class DailyTrainingPO extends MABaseEntity{
    /**
     * 日常培训编号
     */
    @TableId(value = "daily_id")
    private String dailyId;
    /**
     * 发起人编号
     */
    private String initiatorId;
    /**
     * 发起机构编号
     */
    @NotBlank(message = "发起机构不能为空")
    private String orgId;
    /**
     * 发起人名称
     */
    private String initiator;

    /**
     * 发起机构归属一级机构编号
     */
    private String firstBranchId;

    /**
     * 培训年度
     */
    @NotNull(message = "培训年度不能为空")
    @JsonFormat(pattern = "yyyy")
    private Date trainingYear;
    /**
     * 培训类型 0--中央培训，1--内部培训
     */
    @NotNull(message = "培训类型不能为空")
    private Integer trainingType;
    /**
     * 培训名称
     */
    @NotBlank(message = "培训名称不能为空")
    private String trainingName;
    /**
     * 参与人数
     */
    /*@NotNull(message = "参与人数不能为空")
    private Long trainingNumber;*/
    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     * 培训课程
     */
    @TableField(exist = false)
   @NotEmpty(message = "培训课程不能为空")
    private List<DailyTrainingClassPO> classPOS;

    /**
     * 参与培训人员
     */
    @TableField(exist = false)
    @NotEmpty(message = "培训人员不能为空")
    private List<DailyTrainingMemberPO> memberPOS;

    @TableField(exist = false)
    private Integer trainingNumber;
}
