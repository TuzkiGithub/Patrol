package tech.piis.modules.core.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/10/16
 * Time: 9:35
 * Description:被巡视单位-业务次数
 */

@Data
@Accessors(chain = true)
public class UnitsBizCountVO {

    /**
     * 被巡视单位
     */
    private Long unitsId;

    /**
     * 机构编号
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 被巡视单位-次数
     */
    private Integer count;

    /**
     * 来访次数
     */
    private Integer visitCount;

    /**
     * 来电次数
     */
    private Integer callCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    /**
     * 组织类型
     * 0 组务会
     * 1 支部会
     */
    private Integer organizationType;
}
