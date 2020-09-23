package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 巡视组信息 对象 inspection_group
 *
 * @author Kevin
 * @date 2020-09-14
 */

@TableName("inspection_group")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InspectionGroupPO extends PIBaseEntity {

    /**
     * 编号
     */
    @TableId(value = "GROUP_ID")
    private String groupId;

    /**
     * 计划编号
     */
    private String planId;

    /**
     * 巡视组名
     */
    private String groupName;

    /**
     * 组长ID
     */
    private String leaderId;

    /**
     * 组长
     */
    private String leaderName;

    /**
     * 副组长ID
     */
    private String deputyLeaderId;

    /**
     * 副组长
     */
    private String deputyLeaderName;

    /**
     * 被巡视单位
     */
    private List<InspectionUnitsPO> inspectionUnitsList;

    /**
     * 巡视组组员
     */
    private List<InspectionGroupMemberPO> inspectionGroupMemberList;
}