package com.zj.rpcserver.spring.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    String group() default "";
    String version() default "";
}