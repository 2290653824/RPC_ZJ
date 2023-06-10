package com.zj.rpcserver.register.nacos;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.shaded.com.google.j2objc.annotations.AutoreleasePool;
import com.zj.rpccommon.exception.ServiceDiscoveryException;
import com.zj.rpcserver.balance.LoadBalance;
import com.zj.rpcserver.register.ServiceRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengjian
 * @date 2023-06-10 14:20
 */
@Component
@Slf4j
public class NacosServiceRegister implements ServiceRegister {

    @NacosInjected
    private NamingService namingService;

    @Resource(name = "randomLoadBalance")
    private LoadBalance loadBalance;

    @Override
    public void serviceRegister(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String host = inetSocketAddress.getHostName();
        int port = inetSocketAddress.getPort();
        try {
            namingService.registerInstance(rpcServiceName,host,port);
        } catch (NacosException e) {
            log.error("NacosServiceRegister failed,rpcServiceName=[{}],address=[{}],e=[{}]",rpcServiceName,inetSocketAddress,e.getMessage());
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String rpcServiceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(rpcServiceName);
            List<String> hostAndPortList = new ArrayList<>();
            instances.forEach((instance)->{
                String hostAndPort = instance.getIp()+":"+instance.getPort();
                hostAndPortList.add(hostAndPort);
            });
            String target = loadBalance.loadBalance(hostAndPortList);
            String[] hostAndPort = target.split(":");
            return new InetSocketAddress(hostAndPort[0],Integer.parseInt(hostAndPort[1]));
        } catch (NacosException e) {
            throw new ServiceDiscoveryException("nacosDiscovery failed exception:e"+e.getMessage());
        }
    }
}
