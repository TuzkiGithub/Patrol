package tech.piis.modules.managment.domain.vo;
import lombok.Data;

import java.util.Date;

/**
 * ClassName : DailyTrainingVO
 * Package : tech.piis.modules.managment.domain.vo
 * Description :
 *  人员履历中集团培训情况展示VO
 * @author : chenhui@xvco.com
 */
@Data
public class DailyTrainingVO {
    /**
     * 日常培训编号
     */
    private String dailyId;
    /**
     * 人员编号
     */
   // private String memberId;
    /**
     * 培训名称
     */
    private String trainingName;
    /**
     * 培训类型
     */
    private Integer trainingType;
    /**
     * 培训地点(1413/1520/1201)
     */
   // private String trainingPlace;
    /**
     * 职务
     */
    private String memberPost;
    /**
     * 开始时间
     */
    private Date beginDate;
    /**
     * 结束时间
     */
    private Date endDate;
}
