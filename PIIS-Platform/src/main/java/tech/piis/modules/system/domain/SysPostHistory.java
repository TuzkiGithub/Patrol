package tech.piis.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.system.domain
 * User: Tuzki
 * Date: 2020/10/14
 * Time: 14:40
 * Description:用户历史岗位表
 */
@TableName("sys_post_history")
@Data
@Accessors(chain = true)
public class SysPostHistory {

    private Long postHistoryId;

    private String userId;

    private String postName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedTime;

}
