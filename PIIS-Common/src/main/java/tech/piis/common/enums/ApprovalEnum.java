package tech.piis.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.enums
 * User: Tuzki
 * Date: 2020/11/25
 * Time: 18:23
 * Description:审批状态枚举类
 */
public enum ApprovalEnum {

    NO_APPROVAL("无需审批", 1),
    TO_BE_SUBMIT("待提交审批", 2),
    SUBMITTING("审批中", 3),
    PASSED("已通过", 4),
    REJECTED("已驳回", 5);

    private String name;

    private Integer code;

    ApprovalEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
