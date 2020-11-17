package tech.piis.modules.managment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * ClassName : MemberResumePO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("member_resume")
public class MemberResumePO extends MABaseEntity{
    /**
     * 个人编号
     */
    @TableId("member_id")
    private String memberId;
    /**
     * 个人编码
     */
//    @NotBlank(message = "个人编码不能为空")
    private String code;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
    /**
     * 性别 0--男  1--女
     */
    @NotBlank(message = "性别不能为空")
    private int sex;
    /**
     * 出生日期
     */
    @NotNull(message = "出生日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    /**
     * 民族
     */
    @NotBlank(message = "民族不能为空")
    private String nation;
    /**
     * 籍贯
     */
    @NotBlank(message = "籍贯不能为空")
    private String nativePlace;
    /**
     * 出生地
     */
    @NotBlank(message = "出生地不能为空")
    private String birthPlace;
    /**
     * 入党时间
     */
    @NotNull(message = "入党时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinPartDate;
    /**
     * 参加工作时间
     */
    @NotNull(message = "参加工作时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joinWorkDate;
    /**
     * 健康状况
     */
    @NotBlank(message = "健康状况不能为空")
    private String healthStatus;
    /**
     * 专业技术职务
     */
    @NotBlank(message = "专业技术职务不能为空")
    private String professionalSkill;
    /**
     * 熟悉专业
     */
    @NotBlank(message = "熟悉专业不能为空")
    private String familiarSkill;
    /**
     * 联系方式
     */
    @Pattern(regexp = "/^[1][3,4,5,7,8][0-9]{9}$/",message = "手机号码格式不正确")
    @NotBlank(message = "联系方式不能为空")
    private String phone;
    /**
     * 现任职务
     */
    @NotBlank(message = "现任职务不能为空")
    private String post;
    /**
     * 照片
     */
    @NotBlank(message = "照片不能为空")
    private String photo;
    /**
     * 全日制毕业学校
     */
    @NotBlank(message = "全日制毕业学校不能为空")
    private String fullTimeSchool;
    /**
     * 全日制学位
     */
    @NotBlank(message = "全日制学位不能为空")
    private String fullTimeGrade;
    /**
     * 全日制学历
     */
    @NotBlank(message = "全日制学历不能为空")
    private String fullTimeEducation;
    /**
     *
     * 非全日制毕业学校
     */
    @NotBlank(message = "非全日制毕业学校不能为空")
    private String partTimeSchool;
    /**
     *
     * 非全日制学位
     */
    @NotBlank(message = "非全日制学位不能为空")
    private String partTimeGrade;
    /**
     *
     * 非全日制学历
     */
    @NotBlank(message = "非全日制学历不能为空")
    private String partTimeEducation;
    /**
     *
     * 简历
     */
    @NotBlank(message = "简历不能为空")
    private String resume;
    /**
     *
     * 奖惩情况
     */
    @NotBlank(message = "奖惩情况不能为空")
    private String rewardPunishment;
    /**
     *
     * 年度考核情况
     */
    @NotBlank(message = "年度考核情况不能为空")
    private String annualAssessment;

    /**
     * 任命文件编号
     */
    private String appointDocuId;
    /**
     * 角色
     */
    private String memberRole;
    /**
     *
     * 全日制毕业学校
     */
    private String fullTimeSchool1;
    /**
     *
     * 全日制学位
     */
    private String fullTimeGrade1;
    /**
     *
     * 全日制学历
     */
    private String fullTimeEducation1;
;
}
