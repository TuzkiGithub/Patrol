package tech.piis.modules.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.framework.security.LoginUser;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.common.domain
 * User: Tuzki
 * Date: 2020/11/13
 * Time: 8:45
 * Description:IAM认证返回对象
 */
@Data
@Accessors(chain = true)
public class UserAuthResponse {
    /**
     * 系统Token
     */
    private String token;

    /**
     * 用户信息
     */
    private LoginUser loginUser;
}
