package com.zj.rpcserver.register.zookeeper;

import com.zj.rpccommon.exception.ServiceDiscoveryException;
import com.zj.rpcserver.balance.LoadBalance;
import com.zj.rpcserver.register.ServiceRegister;
import com.zj.rpcserver.utils.ZooKeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zhengjian
 * @date 2023-06-09 21:23
 */
@Component
@Slf4j
public class ZookeeperServiceRegister implements ServiceRegister {

    private final static String ZOOKEEPER_RPC_ROOT_PATH = "/rpc";

    @Autowired
    private ZooKeeperUtils zooKeeperUtils;

    @Resource(name = "randomLoadBalance")
    private LoadBalance loadBalance;

    @Override
    public void serviceRegister(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String path = ZOOKEEPER_RPC_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        try {
            zooKeeperUtils.createPersistentNode(path, inetSocketAddress.toString().getBytes());
        } catch (Exception e) {
            log.error("ZookeeperServiceRegister register failed, rpcServiceName = {}, address = {}", rpcServiceName, inetSocketAddress);
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String rpcServiceName) {
        try {
            List<String> serviceList = zooKeeperUtils.getChildren(rpcServiceName);
            if (serviceList.isEmpty()) {
                throw new ServiceDiscoveryException("serviceDiscovery target serviceName:[" + rpcServiceName + "] not found,serviceName");
            }
            String target = loadBalance.loadBalance(serviceList);
            String[] hostAndPost = target.split(":");
            return new InetSocketAddress(hostAndPost[0], Integer.parseInt(hostAndPost[1]));

        } catch (Exception e) {
            throw new ServiceDiscoveryException("serviceDiscovery failed ,e = "+e.getMessage());
        }
    }
}
