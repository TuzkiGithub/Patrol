package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/12/11
 * Time: 16:49
 * Description: 整改公开情况VO
 */
@Data
@Accessors(chain = true)
public class RectificationCountVO {
    private Long unitsId;
    private String orgName;
    private List<PiisDocumentPO> documents;
    private List<PiisDocumentPO> rectificationNoticeDoc;
    private List<PiisDocumentPO> rectificationConditionDoc;
}
