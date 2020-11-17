package tech.piis.modules.core.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/13
 * Time: 19:03
 * Description:一级子公司巡视人员统计VO
 */

@Data
@Accessors(chain = true)
public class PlanMemberCountVO {
    /**
     * 一级子公司ID
     */
    private String planCompanyId;

    /**
     * 一级子公司名称
     */
    private String planCompanyName;

    /**
     * 组长数量
     */
    private Integer leaderCount;

    /**
     * 副组长数量
     */
    private Integer deputyCount;

    /**
     * 联络员数量
     */
    private Integer connectCount;

    /**
     * 组员数量
     */
    private Integer memberCount;

    /**
     * 时间过滤字段
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date beginTime;

    /**
     * 时间过滤字段
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date endTime;
}
