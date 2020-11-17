package tech.piis.modules.managment.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ClassName : RotationExchangePO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *  轮岗交流
 * @author : chenhui@xvco.com
 */
@Data
@TableName("rotation_exchange")
public class RotationExchangePO extends MABaseEntity{
    /**
     * 轮岗交流编号
     */
    @TableId(value = "rotation_id",type = IdType.AUTO)
    private Long rotationId;
    /**
     * 机构编号
     */
    private String orgId;
    /**
     * 所归一级子机构编号
     */
    private String firstbranchId;
    /**
     * 轮岗年度
     */

    private Date rotationYear;
    /**
     * 成员编号
     */
    private String memberId;
    /**
     * 成员姓名
     */
    @NotBlank(message = "成员姓名不能为空")
    private String memberName;
    /**
     *  成员单位
     */
    @NotBlank(message = "成员单位不能为空")
    private String memberUnit;
    /**
     * 成员职务
     */
    @NotBlank(message = "成员职务不能为空")
    private String memberPost;
    /**
     * 联系方式
     */
    @NotBlank(message = "联系方式不能为空")
    private String phoneNo;
    /**
     * 轮岗交流开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /**
     * 轮岗交流结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 交流前单位
     */
    @NotBlank(message = "交流前单位不能为空")
    private String beforeUnit;
    /**
     * 交流后单位
     */
    @NotBlank(message = "交流后单位不能为空")
    private String afterUnit;
    /**
     * 轮岗交流次数
     */
    @TableField(exist = false)
    private Long amount;
}
