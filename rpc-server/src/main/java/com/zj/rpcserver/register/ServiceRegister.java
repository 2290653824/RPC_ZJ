package com.zj.rpcserver.register;

import java.net.InetSocketAddress;

/**
 * @author zhengjian
 * @date 2023-06-09 21:01
 */
public interface ServiceRegister {

    /**
     * 将相关的数据注册到注册中心中
     * @param serviceName
     * @param inetSocketAddress
     */
    void serviceRegister(String serviceName , InetSocketAddress inetSocketAddress);

    /**
     * 根据服务名发现相应的地址
     * @param serviceName interfaceName+group+version
     * @return
     */
    InetSocketAddress serviceDiscovery(String serviceName);
}
