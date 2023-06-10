package com.zj.rpccommon.dto;

import lombok.*;

/**
 * @author zhengjian
 * @date 2023-06-10 10:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RpcConfig {

    private String version;
    private String group;
    private Object service;

    public String getRpcServiceName(){
        return getServiceName()+this.getGroup()+this.getVersion();
    }

    public String getServiceName(){
        return service.getClass().getInterfaces()[0].getCanonicalName();
    }


}
