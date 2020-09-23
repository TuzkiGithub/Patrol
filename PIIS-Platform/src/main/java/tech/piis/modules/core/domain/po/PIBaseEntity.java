package tech.piis.modules.core.domain.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.core.domain.po
 * User: Tuzki
 * Date: 2020/9/17
 * Time: 18:47
 * Description:
 */
@Data
@Accessors(chain = true)
public class PIBaseEntity {
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    /**
     * 租户编号
     */
    private String entId;

    private Integer pageNum;

    private Integer pageSize;

}
