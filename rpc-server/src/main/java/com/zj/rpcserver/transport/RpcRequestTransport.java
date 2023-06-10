package com.zj.rpcserver.transport;

import com.zj.rpccommon.dto.RpcRequest;

/**
 * @author zhengjian
 * @date 2023-06-10 9:16
 */
public interface RpcRequestTransport {

    /**
     * 调用底层的网路传输细节，发送rpcRequest，返回对应的对象
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);
}
