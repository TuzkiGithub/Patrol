package tech.piis.modules.core.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.vo
 * User: Tuzki
 * Date: 2020/12/8
 * Time: 15:31
 * Description:公告信息总览VO
 */
@Data
@Accessors(chain = true)
public class AnnouncementInfoCountVO {
    private Long unitsId;
    private String orgName;

    private List<PiisDocumentPO> documents;
}
