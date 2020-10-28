package top.chengdongqing.common.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件配置
 *
 * @author Luyao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Events {

    /**
     * 事件名称
     */
    String[] value();

    /**
     * 是否异步执行
     */
    boolean async() default true;
}
