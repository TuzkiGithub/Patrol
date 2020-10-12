package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/10/9
 * Time: 17:13
 * Description: 巡察统计VO
 */
@Data
@Accessors(chain = true)
public class PlanCompanyCountVO {
    private String companyId;

    private String companyName;

    private int count;
}
