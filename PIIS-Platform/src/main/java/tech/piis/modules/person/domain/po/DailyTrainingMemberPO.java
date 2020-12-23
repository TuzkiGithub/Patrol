package tech.piis.modules.person.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import tech.piis.modules.core.domain.po.PIBaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * ClassName : DailyTrainingMemberPO
 * Package : tech.piis.modules.person.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("person_daily_training_member")
public class DailyTrainingMemberPO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    /**
     *  培训人员编号
     */
    @NotEmpty(message = "培训人员编号不能为空")
    private String memberId;
    /**
     * 日常培训编号
     */
    @NotEmpty(message = "日常培训编号不能为空")
    private String dailyId;
    /**
     * 分组名
     */
    @NotEmpty(message = "分组名不能为空")
    private String groupName;
    /**
     * 培训人员姓名
     */
    @NotEmpty(message = "培训人员姓名不能为空")
    private String memberName;
    /**
     * 培训人员单位
     */
    @NotEmpty(message = "培训人员单位不能为空")
    private String memberUnit;
    /**
     * 培训人员职务
     */
    @NotEmpty(message = "培训人员职务不能为空")
    private String memberPost;
    /**
     * 组内职务
     */
    @NotEmpty(message = "组内职务不能为空")
    private String innerPost;
    /**
     * 性别
     */
    @NotEmpty(message = "性别不能为空")
    private String sex;
    /**
     * 民族
     */
    @NotEmpty(message = "民族不能为空")
    private String nation;
    /**
     * 政治面貌
     */
    @NotEmpty(message = "政治面貌不能为空")
    private String nactive;
    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 电话号码d
     */
    @Pattern(regexp = "/^[1][3,4,5,7,8][0-9]{9}$/",message = "手机号码格式不正确")
    @NotBlank(message = "电话号码不能为空")
    private String phoneNo;
    /**
     * 操作类型 (1--新增  2--修改 3--删除)
     */
    @TableField(exist = false)
    private Integer operationType;

}
