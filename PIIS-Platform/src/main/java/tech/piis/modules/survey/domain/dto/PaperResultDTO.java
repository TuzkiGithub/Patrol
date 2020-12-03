package tech.piis.modules.survey.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import tech.piis.modules.core.domain.po.PIBaseEntity;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.survey.domain.dto
 * User: Tuzki
 * Date: 2020/11/20
 * Time: 16:41
 * Description:问卷/测评参数类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Accessors(chain = true)
public class PaperResultDTO extends PIBaseEntity {
    private String paperId;
}
