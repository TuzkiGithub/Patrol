package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/17
 * Time: 14:59
 * Description:巡视巡察项目、被巡视单位、巡视组数量VO
 */
@Data
@Accessors(chain = true)
public class PiisProjectCountVO {

    /**
     * 公司名称
     */
    private String orgName;

    /**
     * 项目数量
     */
    private Integer planCount;

    /**
     * 巡视组数量
     */
    private Integer groupCount;

    /**
     * 被巡视单位数量
     */
    private Integer unitsCount;
}
