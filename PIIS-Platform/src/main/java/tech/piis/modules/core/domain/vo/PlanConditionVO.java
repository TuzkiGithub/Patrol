package tech.piis.modules.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/17
 * Time: 8:40
 * Description:巡视/巡察参与情况VO
 */
@Data
@Accessors(chain = true)
public class PlanConditionVO {

    /**
     * 巡视参与类型
     */
    private String piisType;

    /**
     * 巡视方案名称
     */
    private String planName;

    /**
     * 巡视组名称
     */
    private String groupName;

    /**
     * 组内担任职务
     */
    private String groupPost;

    /**
     * 组长职务
     */
    private String leaderPost;

    /**
     * 副组长职务
     */
    private String deputyPost;

    /**
     * 联络员职务
     */
    private String connectorPost;

    /**
     * 组员职务
     */
    private String memberPost;

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
}
