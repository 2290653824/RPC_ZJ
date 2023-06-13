package com.zj.rpcserver.compress;

/**
 * @author zhengjian
 * @date 2023-06-13 19:06
 */
public interface Compress {

    public byte[] compress(byte[] bytes);

    public byte[] decompress(byte[] bytes);
}
