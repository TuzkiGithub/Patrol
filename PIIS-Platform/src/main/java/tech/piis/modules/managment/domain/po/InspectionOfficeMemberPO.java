package tech.piis.modules.managment.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import tech.piis.modules.core.domain.po.PiisDocumentPO;

import java.util.List;

/**
 * ClassName : InspectionOfficeMemberPO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@Data
@TableName("inspection_office_member")
public class InspectionOfficeMemberPO extends MABaseEntity{
    /**
     * 巡视/巡察办成员编号
     */
    @TableId("inspection_member_id")
    private String inspectionMemberId;
    /**
     * 成员所属机构编号
     */
    private String orgId;
    /**
     * 成员编号
     */
    private String memberId;
    /**
     * 成员名称
     */
    private String memberName;
    /**
     * 成员职务
     */
    private String memberPost;
    /**
     * 成员角色
     */
    private String memberRole;

    private String inspectionOfficeId;
    /**
     * 任命文件对象
     */
    @TableField(exist = false)
    private List<PiisDocumentPO> piisDocumentPO;
}
