package com.zj.rpccommon.exception;

/**
 * @author zhengjian
 * @date 2023-06-09 21:40
 */
public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(String message){
        super(message);
    }
}
