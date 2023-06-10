package com.zj.rpcserver.spring.annotation;

import java.lang.annotation.*;

/**
 * @author zhengjian
 * @date 2023-06-10 15:14
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    String group() default "";
    String version() default "";
}
