package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/11/24
 * Time: 9:03
 * Description:巡视方案VO
 */
@Data
@Accessors(chain = true)
public class PatrolBriefVO {
    private Long objectId;

    private List<PiisDocumentPO> documents;
}
