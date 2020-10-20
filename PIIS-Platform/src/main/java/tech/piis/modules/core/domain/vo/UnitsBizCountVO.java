package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

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
}
