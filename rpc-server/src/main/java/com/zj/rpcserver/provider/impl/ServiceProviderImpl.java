package com.zj.rpcserver.provider.impl;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.zj.rpccommon.dto.RpcConfig;
import com.zj.rpcserver.provider.ServiceProvider;
import com.zj.rpcserver.register.ServiceRegister;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengjian
 * @date 2023-06-10 15:53
 */
@Component
@Slf4j
public class ServiceProviderImpl implements ServiceProvider, InitializingBean {

    @Resource(name = "nacosServiceRegister")
    private ServiceRegister serviceRegister;

    @Value("${rpc.service.port}")
    private int port;

    private Set<String> registeredService;
    private Map<String,Object> registeredServiceMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.registeredService=new ConcurrentHashSet<String>();
        this.registeredServiceMap=new ConcurrentHashMap<String,Object>();
    }

    @Override
    public void addService(RpcConfig rpcConfig) {
        String rpcServiceName = rpcConfig.getRpcServiceName();
        if(registeredService.contains(rpcServiceName)){
            return;
        }
        registeredService.add(rpcServiceName);
        registeredServiceMap.put(rpcServiceName,rpcConfig.getService());
        log.info("addService successful,rpc:[{}]",rpcConfig);
    }

    @SneakyThrows
    @Override
    public Object getService(String rpcServiceName) {
        if(!registeredService.contains(rpcServiceName)){
            throw new Exception(rpcServiceName+" has not registered");
        }
        return registeredServiceMap.get(rpcServiceName);
    }

    @Override
    public void publishService(RpcConfig rpcConfig) {
        try {
            this.addService(rpcConfig);
            String host = InetAddress.getLocalHost().getHostAddress();
            serviceRegister.serviceRegister(rpcConfig.getRpcServiceName(),
                    new InetSocketAddress(host,port));
        } catch (Exception e) {
            log.error("publishService error: "+e.getMessage());
        }

    }


}
