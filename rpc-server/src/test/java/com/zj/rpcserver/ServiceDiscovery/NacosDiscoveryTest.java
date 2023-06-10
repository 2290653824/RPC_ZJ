package com.zj.rpcserver.ServiceDiscovery;

import com.zj.rpcserver.register.ServiceRegister;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author zhengjian
 * @date 2023-06-10 14:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class NacosDiscoveryTest {


    @Resource(name = "nacosServiceRegister")
    private ServiceRegister serviceRegister;

    @Test
    public void test(){
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        String rpcServiceName="findUserById_version1";
        serviceRegister.serviceRegister(rpcServiceName,inetSocketAddress);

        InetSocketAddress res = serviceRegister.serviceDiscovery(rpcServiceName);


    }
}
