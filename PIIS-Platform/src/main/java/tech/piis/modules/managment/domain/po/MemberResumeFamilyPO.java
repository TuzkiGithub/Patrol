package tech.piis.modules.managment.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName : MemberResumeFamilyPO
 * Package : tech.piis.modules.managment.domain
 * Description :
 *
 * @author : chenhui@xvco.com
 */
@TableName("member_resume_family")
@Data
public class MemberResumeFamilyPO extends MABaseEntity{
    /**
     * 家庭成员情况编号
     */
    @TableId(value = "family_id",type = IdType.AUTO)
    private Long familyId;
    /**
     * 人员编号
     */
    private String memberId;
    /**
     * 人员关系称谓
     */
    private String relation;
    /**
     * 姓名
     */
    private String name;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 年龄
     */
    private String age;
    /**
     * 政治面貌
     */
    private String policitionOutlook;
    /**
     * 工作单位
     */
    private String workUnit;
    /**
     * 职务
     */
    private String workPost;




}
