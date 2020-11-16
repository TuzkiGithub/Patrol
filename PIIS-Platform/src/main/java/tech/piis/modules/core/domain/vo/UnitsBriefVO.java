package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/2
 * Time: 10:03
 * Description:巡视单位简要信息
 */
@Data
@Accessors(chain = true)
public class UnitsBriefVO {
    @NotBlank(message = "被巡视单位ID不能为空！")
    private String unitsId;

    @NotBlank(message = "被巡视单位名称不能为空！")
    private String unitsName;
}
