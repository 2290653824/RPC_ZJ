package com.zj.rpcserver.provider;

import com.zj.rpccommon.dto.RpcConfig;

/**
 * @author zhengjian
 * @date 2023-06-10 15:49
 */
public interface ServiceProvider {

    /**
     * when the server started, add the service into java cache
     * @param rpcConfig
     */
    void addService(RpcConfig rpcConfig);

    /**
     * get the service according to rpcServiceName
     * @param rpcServiceName
     * @return
     */
    Object getService(String rpcServiceName);

    /**
     * whe the server started ,add the service into register
     * @param rpcConfig
     */
    void publishService(RpcConfig rpcConfig);
}
