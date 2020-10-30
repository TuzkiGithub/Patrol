package tech.piis.modules.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.common.domain
 * User: Tuzki
 * Date: 2020/10/29
 * Time: 11:06
 * Description:Iam返回类
 */
@Data
@Accessors(chain = true)
public class IamResponse {
    private String Result;

    private String ResultCode;

    private String ReturnMessage;
}
