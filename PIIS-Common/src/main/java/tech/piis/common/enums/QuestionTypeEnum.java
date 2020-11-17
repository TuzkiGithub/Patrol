package tech.piis.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.enums
 * User: Tuzki
 * Date: 2020/11/10
 * Time: 11:20
 * Description:题目类型枚举类
 */
public enum QuestionTypeEnum {
    SINGLE(1, "单选题"),
    DOUBLE(2, "多选题"),
    JUDGE(3, "判断题"),
    BLANK(4, "填空题"),
    QA(5, "问答题");

    private String type;
    private Integer code;

    QuestionTypeEnum(Integer code, String type) {
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
