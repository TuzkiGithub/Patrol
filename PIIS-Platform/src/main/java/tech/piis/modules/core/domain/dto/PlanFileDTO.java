package tech.piis.modules.core.domain.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.dto
 * User: Tuzki
 * Date: 2020/9/27
 * Time: 15:48
 * Description:文件上传DTO
 */

@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class PlanFileDTO {

    /**
     * 文件
     */
    private MultipartFile file;

    /**
     * 操作类型
     * 1：新增 2：删除
     */
    private Integer type;
}
