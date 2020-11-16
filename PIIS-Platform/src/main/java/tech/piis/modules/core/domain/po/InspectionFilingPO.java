package tech.piis.modules.core.domain.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.po
 * User: Tuzki
 * Date: 2020/11/3
 * Time: 18:55
 * Description:立卷归档实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionFilingPO extends PIBaseEntity {
    /**
     * 计划编号
     */
    private String planId;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 巡视组编号
     */
    private String groupId;

    /**
     * 巡视组名
     */
    private String groupName;

    /**
     * 巡视组组长姓名
     */
    private String leaderName;

    /**
     * 被巡视单位ID
     */
    private String unitsId;

    /**
     * 被巡视单位名称
     */
    private String orgName;

    /**
     * 党委汇报材料数
     */
    private Integer committeeMeetingsCount;

    /**
     * 重要问题专题报告数
     */
    private Integer importReportCount;

    /**
     * 谈话人数
     */
    private Integer talkCount;

    /**
     * 问题底稿数
     */
    private Integer problemDraftCount;

    /**
     *  领导干部个人事项抽查数
     */
    private Integer checkPersonalCount;

    /**
     * 线索数
     */
    private Integer clueCount;

    /**
     * 调阅文件数
     */
    private Integer consultCount;

    /**
     *
     * 信访件数
     */
    private Integer visitCount;


}
