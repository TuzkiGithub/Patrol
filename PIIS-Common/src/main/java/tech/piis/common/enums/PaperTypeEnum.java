package tech.piis.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.enums
 * User: Tuzki
 * Date: 2020/11/11
 * Time: 9:11
 * Description:试卷类型枚举类
 */
public enum PaperTypeEnum {
    EVALUATION(1, "测评"),
    QUESTIONNAIRE(2, "问卷");

    private String type;
    private Integer code;

    PaperTypeEnum(Integer code, String type) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
