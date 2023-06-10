package com.zj.rpccommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;

/**
 * @author zhengjian
 * @date 2023-06-09 19:36
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class RpcRequest implements Serializable {
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class <?>[] parametersType;
    private String version;
    private String group;

    public String getRpcServiceName(){
        return this.getInterfaceName()+this.getGroup()+this.getVersion();
    }
}
