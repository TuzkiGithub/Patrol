package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 谈话提纲对象 inspection_talk_outline
 *
 * @author Tuzki
 * @date 2020-11-04
 */

@TableName("inspection_talk_outline")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionTalkOutlinePO extends PIBaseEntity {
    /**
     * 谈话提纲编号
     */
    @TableId(value = "TALK_OUTLINE_ID", type = IdType.AUTO)
    private Long talkOutlineId;

    /**
     * 谈话分类ID，自关联使用
     */
    private Long talkClassificationId;

    /**
     * 计划ID
     */
    @NotBlank(message = "巡视计划ID不能为空！")
    private String planId;

    /**
     * 谈话分类
     */
    @NotBlank(message = "谈话分类不能为空！")
    private String talkClassification;

    /**
     * 谈话问题
     */
    private String talkQuestion;

    /**
     * 谈话问题
     */
    private String talkAnswer;

    /**
     * 谈话问题
     */
    List<InspectionTalkOutlinePO> questionList;

    /**
     * 摘要
     */
    @TableField(exist = false)
    private String talkAbstract;


    @TableField(exist = false)
    @NotNull(message = "操作类型不能为空！")
    private Integer operationType;
}
