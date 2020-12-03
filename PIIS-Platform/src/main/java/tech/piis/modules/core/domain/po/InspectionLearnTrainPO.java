package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 学习培训 对象 inspection_learn_train
 *
 * @author Tuzki
 * @date 2020-11-23
 */

@TableName("inspection_learn_train")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class InspectionLearnTrainPO extends PIBaseEntity {
    /**
     * 学习培训编号
     */
    @TableId(value = "LEARN_TRAIN_ID", type = IdType.AUTO)
    private Long learnTrainId;
    /**
     * 计划ID
     */
    private String planId;
    /**
     * 培训名称
     */
    private String trainName;
    /**
     * 培训地点
     */
    private String trainPlace;
    /**
     * 培训开始时间
     */
    private Date beginTime;
    /**
     * 培训结束时间
     */
    private Date endTime;

    @TableField(exist = false)
    private List<PiisDocumentPO> documents;
}
