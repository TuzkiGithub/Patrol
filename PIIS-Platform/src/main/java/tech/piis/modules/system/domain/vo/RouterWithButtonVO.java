package tech.piis.modules.system.domain.vo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.system.domain.vo
 * User: Tuzki
 * Date: 2020/11/19
 * Time: 14:47
 * Description:路由配置信息包含菜单
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class RouterWithButtonVO {
    /**
     * 菜单
     */
    private List<RouterVo> router;

    /**
     * 按钮
     */
    private String[] button;

    /**
     * 1：巡视
     * 2：巡察
     * 3：ALL
     */
    private Integer patrolType;
}
