package tech.piis.modules.core.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/30
 * Time: 11:18
 * Description:巡视简要计划参数
 */
@Data
@Accessors(chain = true)
public class PlanBriefDTO {
    private String planId;
    private Long unitsId;
    private String userId;
}
