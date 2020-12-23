package tech.piis.modules.core.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Set;

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
     * 巡视计划ID参数，数据过滤使用
     */
    @TableField(exist = false)
    private Set<String> planIdDataScope;

    /**
     * 巡视组ID参数，数据过滤使用
     */
    @TableField(exist = false)
    private Set<String> groupIdDataScope;

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

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;
}
