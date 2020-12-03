package tech.piis.framework.annotation;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * Package: tech.piis.framework.annotation
 * User: Tuzki
 * Date: 2020/12/1
 * Time: 18:28
 * Description:数据权限过滤注解
 */
@Target(ElementType.METHOD) //标注在方法上
@Retention(RetentionPolicy.RUNTIME) //运行时注解
@Documented
public @interface DataPermission {
}
