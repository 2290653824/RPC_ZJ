package com.zj.rpccommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

/**
 * @author zhengjian
 * @date 2023-06-09 19:42
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class RpcResponse<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public RpcResponse success(T data){
        return RpcResponse.builder().code(200).message(message).data(data).build();
    }

    public RpcResponse failed(String message){
        return RpcResponse.builder().code(500).message(message).build();
    }
}
