package tech.piis.modules.managment.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * ClassName : RecommendBest
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@TableName(value = "recommend_best")
@Data
public class RecommendBestPO extends MABaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    /**
     * 机构编号
     */
    private String orgId;
    /**
     * 年度
     */
    @NotNull(message = "年度不能为空")
    @JsonFormat(pattern = "yyyy")
    private Date recommendYear;
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
     * 成员单位
     */
    @NotBlank(message = "所在单位不能为空")
    private String memberUnit;
    /**
     * 成员职位
     */
    @NotBlank(message = "所在职位不能为空")
    private String memberPost;
    /**
     * 择优推荐类型 0 中巡办培训 1 中央巡视 2 中巡办干部交流 3 其他
     */
    @NotNull(message = "择优推荐类型不能为空")
    private Integer recommendType;
    /**
     * 培训版名称
     */
    @NotNull(message = "培训版名称不能为空")
    private String trainingName;
    /**
     * 培训开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "培训开始日期不能为空")
    private Date beginDate;
    /**
     * 培训结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "培训结束日期不能为空")
    private Date endDate;
    /**
     * 培训地点
     */
    @NotNull(message = "培训地点不能为空")
    private String trainingPlace;
    /**
     * 备注
     */
    private String remark;

}
