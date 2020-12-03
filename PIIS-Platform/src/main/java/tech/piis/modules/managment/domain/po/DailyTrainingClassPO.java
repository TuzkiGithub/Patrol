package tech.piis.modules.managment.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * ClassName : DailyTrainingClassPO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("daily_training_class")
public class DailyTrainingClassPO extends MABaseEntity{
    /**
     * 培训课程编号
     */
    @TableId(value = "class_id")
    private String classId;
    /**
     * 日常培训编号
     */
    @NotBlank(message = "日常培训编号不能为空")
    private String dailyId;
    /**
     * 培训日期
     */
    @NotNull(message = "培训日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;
    /**
     * 培训时间 0--上午 1--下午
     */
    @NotEmpty(message = "培训时间不能为空")
    private Integer trainingTime;
    /**
     * 培训时间区间
     */
    @NotBlank(message = "培训时间区间不能为空")
    private String trainingSection;
    /**
     * 授课人姓名
     */
    @NotBlank(message = "授课人姓名不能为空")
    private String lecturer;
    /**
     * 授课人单位
     */
    @NotBlank(message = "授课人单位不能为空")
    private String lecturerUnit;
    /**
     * 授课人职位
     */
//    @NotBlank(message = "授课人职位不能为空")
    private String lecturerPost;
    /**
     * 授课地点
     */
    @NotBlank(message = "授课地点不能为空")
    private String lecturerPlace;
    /**
     * 参与人员
     */
    @NotEmpty(message = "参与人员不能为空")
    @TableField(exist = false)
    private List<String> memberList;
    /**
     * 参与人员
     */
    private String members;
    /**
     * 课件编号
     */
//    private String piisDocuId;


    @NotEmpty(message = "课件文件对象不能为空")
    @TableField(exist = false)
    private List<PiisDocumentPO> piisDocumentPOS;
    /**
     * 操作类型 (1--新增  2--修改 3--删除)
     */
    @TableField(exist = false)
    private Integer operationType;
}
