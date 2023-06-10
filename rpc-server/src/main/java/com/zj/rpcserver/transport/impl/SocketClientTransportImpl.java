package com.zj.rpcserver.transport.impl;

import com.zj.rpccommon.dto.RpcRequest;
import com.zj.rpcserver.balance.LoadBalance;
import com.zj.rpcserver.register.ServiceRegister;
import com.zj.rpcserver.transport.RpcRequestTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhengjian
 * @date 2023-06-10 9:23
 */
@Component
public class SocketClientTransportImpl implements RpcRequestTransport {

    @Resource(name = "zookeeperServiceRegister")
    private ServiceRegister serviceRegister;

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceRegister.serviceDiscovery(rpcRequest.getRpcServiceName());
        try(Socket socket=new Socket()){
            socket.connect(inetSocketAddress);
            ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcRequest);
            outputStream.flush();

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return inputStream.readObject();

        }catch (Exception e) {
            throw new RuntimeException("sendRequest failed:"+e.getMessage());
        }
    }
}
