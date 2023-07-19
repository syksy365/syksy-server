package site.syksy.qingzhou.web.resolver;

import java.lang.annotation.*;

/**
 * @author Raspberry
 * 获取当前请求登录的用户ID注解
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {
    boolean required() default true;
}
