package com.zj.rpcserver.proxy;

import com.zj.rpccommon.dto.RpcConfig;
import com.zj.rpccommon.dto.RpcRequest;
import com.zj.rpccommon.dto.RpcResponse;
import com.zj.rpcserver.transport.RpcRequestTransport;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author zhengjian
 * @date 2023-06-10 9:06
 */
@Slf4j
public class RpcClientInvocationHandler implements InvocationHandler {

    private RpcRequestTransport rpcRequestTransport;
    private RpcConfig rpcConfig;

    public RpcClientInvocationHandler(RpcRequestTransport rpcRequestTransport, RpcConfig rpcConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcConfig = rpcConfig;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[] {clazz},this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("start to invoke method:[{}]",method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().requestId(UUID.randomUUID().toString())
                .group(rpcConfig.getGroup())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parametersType(method.getParameterTypes())
                .parameters(args)
                .version(rpcConfig.getVersion())
                .build();

        RpcResponse response = (RpcResponse)rpcRequestTransport.sendRequest(rpcRequest);
        return response.getData();
    }

    
}
