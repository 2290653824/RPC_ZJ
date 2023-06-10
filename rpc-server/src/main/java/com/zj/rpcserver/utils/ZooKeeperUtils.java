package com.zj.rpcserver.utils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ZooKeeperUtils implements InitializingBean {

    private ZooKeeper zooKeeper;

    @Value("${zookeeper.host}")
    private String host;

    @Value("${zookeeper.port}")
    private String port;

    @Override
    public void afterPropertiesSet() throws Exception {
        String connectionString = host+":"+port;
        this.zooKeeper = new ZooKeeper(connectionString, 5000, null);
    }

    public void createPersistentNode(String path, byte[] data) throws KeeperException, InterruptedException {
        zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void createEphemeralNode(String path, byte[] data) throws KeeperException, InterruptedException {
        zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }

    public byte[] getNodeData(String path) throws KeeperException, InterruptedException {
        return zooKeeper.getData(path, false, null);
    }

    public void updateNodeData(String path, byte[] data) throws KeeperException, InterruptedException {
        zooKeeper.setData(path, data, zooKeeper.exists(path, false).getVersion());
    }

    public void deleteNode(String path) throws KeeperException, InterruptedException {
        zooKeeper.delete(path, zooKeeper.exists(path, false).getVersion());
    }

    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        return zooKeeper.getChildren(path, false);
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }

}
