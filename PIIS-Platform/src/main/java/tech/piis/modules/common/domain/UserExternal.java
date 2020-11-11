package tech.piis.modules.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.common.domain
 * User: Tuzki
 * Date: 2020/10/8
 * Time: 16:37
 * Description:用户数据实体（数据港）
 */

@Data
@Accessors(chain = true)
public class UserExternal {
    /**
     * 集团员工统一编号
     */
    private String grpEmpUnifId;

    /**
     * 员工姓名
     */
    private String empNm;

    /**
     * 性别代码
     * 未说明	0
     * 男	1
     * 女	2
     * 未知	3
     */
    private String genderCd;

    /**
     * 工作电话
     */
    private String workTel;

    /**
     * 手机号码
     */
    private String moblNum;

    /**
     * 现任职务
     */
    private String currPostn;

    /**
     * 所属集团部门编号
     */
    private String belgGrpDeptId;

    /**
     * 员工邮箱
     */
    private String empEml;

    /**
     * 在职状态代码
     * 主动离职	0
     * 在职	1
     * 退休	2
     * 系统内调动	3  -> 虚拟节点
     * 死亡	4
     * 辞退	5
     * 其它	6
     * 兼职	7
     * 借调	8
     */
    private String dutyStatCd;
}
