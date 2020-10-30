package tech.piis.modules.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.common.domain
 * User: Tuzki
 * Date: 2020/10/29
 * Time: 10:36
 * Description:IAM同步用户返回类
 */
@Data
@Accessors(chain = true)
public class IamUser {
    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 上级组织ID
     */
    private String orgCode;

    /**
     * 上级组织名称
     */
    private String orgName;
}
