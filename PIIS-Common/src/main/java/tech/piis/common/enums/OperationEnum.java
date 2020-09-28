package tech.piis.common.enums;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.common.enums
 * User: Tuzki
 * Date: 2020/9/24
 * Time: 9:47
 * Description:数据库操作类型枚举类
 */
public enum OperationEnum {
    INSERT("INSERT",1),UPDATE("UPDATE",2),DELETE("DELETE",3);


    private String name;

    private int code;

    OperationEnum(String name,int code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
