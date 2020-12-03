package tech.piis.modules.workflow.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.workflow.domain.vo
 * User: Tuzki
 * Date: 2020/12/2
 * Time: 15:22
 * Description:代办数量VO
 */
@Data
@Accessors(chain = true)
public class TodoCountVO {
    private Integer undoCount;

    private Integer doCount;
}
