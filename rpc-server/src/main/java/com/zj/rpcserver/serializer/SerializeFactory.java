package com.zj.rpcserver.serializer;

import com.zj.rpccommon.dto.rpcProtocal.enums.SerializeTypeEnum;
import com.zj.rpcserver.serializer.impl.JDKSerializer;

import java.util.Map;

/**
 * @author zhengjian
 * @date 2023-06-13 18:17
 */
public class SerializeFactory {

    private static Map<Byte,Object> serializeMap;

    static {
        serializeMap.put(SerializeTypeEnum.JDK.getSerializeType(), new JDKSerializer());
    }
    public static Serializer getSerializer(byte serializeType){
        return (Serializer) serializeMap.get(serializeType);
    }

}
