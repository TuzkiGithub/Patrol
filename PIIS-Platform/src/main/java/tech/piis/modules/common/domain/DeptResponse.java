package tech.piis.modules.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.common.domain
 * User: Tuzki
 * Date: 2020/10/9
 * Time: 11:02
 * Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeptResponse extends DeptExternal{
    private int code;

    private String msg;

    private List<DeptExternal> data;
}
