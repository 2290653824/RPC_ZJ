package com.zj.rpcserver.spring.postProcessor;

import com.zj.rpccommon.dto.RpcConfig;
import com.zj.rpcserver.provider.ServiceProvider;
import com.zj.rpcserver.proxy.RpcClientInvocationHandler;
import com.zj.rpcserver.spring.annotation.RpcReference;
import com.zj.rpcserver.spring.annotation.RpcService;
import com.zj.rpcserver.transport.RpcRequestTransport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author zhengjian
 * @date 2023-06-10 16:49
 */
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private ServiceProvider serviceProvider;

    @Autowired
    private RpcRequestTransport rpcRequestTransport;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)){
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcConfig rpcConfig = RpcConfig.builder()
                    .version(rpcService.version())
                    .group(rpcService.group())
                    .service(bean).build();
            serviceProvider.publishService(rpcConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(RpcReference.class)) {
                RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                RpcConfig rpcConfig = RpcConfig.builder()
                        .version(rpcReference.version())
                        .group(rpcReference.group())
                        .build();

                RpcClientInvocationHandler rpcClientInvocationHandler = new RpcClientInvocationHandler(rpcRequestTransport, rpcConfig);
                Object proxy = rpcClientInvocationHandler.getProxy(field.getType());
                field.setAccessible(true);
                try {
                    field.set(bean,proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
