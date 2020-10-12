package tech.piis.modules.system.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.system.domain
 * User: Tuzki
 * Date: 2020/10/10
 * Time: 16:19
 * Description:
 */
@Data
@Accessors(chain = true)
public class SysUserPostVO {
    /** 用户ID */
    private String userId;

    /** 岗位ID */
    private String postId;

    /**
     * 岗位名称
     */
    private String postName;
}
