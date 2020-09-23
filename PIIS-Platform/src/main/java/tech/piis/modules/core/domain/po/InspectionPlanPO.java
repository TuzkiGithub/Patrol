package tech.piis.modules.core.domain.po;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 巡视计划 对象 inspection_plan
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_plan")
@Data
@Accessors(chain = true)
public class InspectionPlanPO extends PIBaseEntity {
    /**
     * 编号
     */
    @TableId(value = "PLAN_ID")
    private String planId;
    /**
     * 方案名称
     */
    private String planName;
    /**
     * 预计开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 预计结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 巡视状态 0未开始，1进行中，2结束
     */
    private int status;
    /**
     * 方案内容
     */
    private String planDesc;

    /**
     * 巡视组
     */
    private List<InspectionGroupPO> inspectionGroupList;

}