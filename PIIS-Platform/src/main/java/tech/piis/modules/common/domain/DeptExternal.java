package tech.piis.modules.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.modules.common.domain
 * User: Tuzki
 * Date: 2020/10/9
 * Time: 9:39
 * Description:部门数据实体（数据港）
 */
@Data
@Accessors(chain = true)
public class DeptExternal {

    /**
     * 集团机构编号
     */
    private String grpOrgId;

    /**
     * 上级集团机构编号
     */
    private String upGrpOrgId;

    /**
     * 机构简称
     */
    private String orgShtNm;

    /**
     * 机构全称
     */
    private String orgFullNm;

    /**
     * 机构状态
     * 取消	0	属于撤销状态
     * 正常	1	属于正常状态
     * 撤消	2	属于撤销状态
     * 合并	3	属于撤销状态
     * 清算	4	属于撤销状态
     * 筹备	5	属于正常状态
     * 停业	6	属于撤销状态
     * 休眠	7	属于撤销状态
     * 其它	8	属于撤销状态
     */
    private String orgStatCd;

    /**
     * 机构编号层级串
     */
    private String orgIdHrcyStr;

}
