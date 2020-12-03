package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/30
 * Time: 11:18
 * Description:巡视简要计划
 */
@Data
@Accessors(chain = true)
public class PlanBriefVO {
    private String planId;
    private String planName;
    private String groupId;
    private String groupName;
    private Long unitsId;
    private String orgId;
    private String orgName;
}
