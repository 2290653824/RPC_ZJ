package com.zj.rpcserver.compress;

import com.zj.rpccommon.dto.rpcProtocal.enums.CompressTypeEnum;
import com.zj.rpcserver.compress.impl.GZipCompress;
import com.zj.rpcserver.serializer.Serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhengjian
 * @date 2023-06-13 19:21
 */
public class CompressFactory {

    static Map<Byte,Object> compressMap;

    static {
        compressMap = new HashMap<>();
        compressMap.put(CompressTypeEnum.GZIP.getCompressTypeNum(),new GZipCompress());
    }

    public static Compress getCompress(Byte compressType){
        return (Compress) compressMap.get(compressType);
    }


}
