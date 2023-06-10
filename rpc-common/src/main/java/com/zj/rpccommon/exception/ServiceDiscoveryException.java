package com.zj.rpccommon.exception;

/**
 * @author zhengjian
 * @date 2023-06-10 8:42
 */
public class ServiceDiscoveryException extends RuntimeException{

    public ServiceDiscoveryException(String message){
        super(message);
    }
}
