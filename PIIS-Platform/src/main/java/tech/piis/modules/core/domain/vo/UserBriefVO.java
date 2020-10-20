package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/10/15
 * Time: 10:05
 * Description:人员简要信息
 */

@Data
@Accessors(chain = true)
public class UserBriefVO {
    @NotBlank(message = "人员ID不能为空！")
    private String userId;

    @NotBlank(message = "人员姓名不能为空！")
    private String userName;
}
