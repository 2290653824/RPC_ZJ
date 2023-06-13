package com.zj.rpcserver.compress.impl;

import com.zj.rpcserver.compress.Compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author zhengjian
 * @date 2023-06-13 19:10
 */
public class GZipCompress implements Compress{


    private static final int BUFFER_SIZE = 1024 * 4;
    @Override
    public byte[] compress(byte[] bytes) {
        if(bytes == null) throw new NullPointerException("bytes is null");
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)){
            gzipOutputStream.write(bytes);
            gzipOutputStream.flush();
            gzipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error",e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        //写出compress对应的decompress方法
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error", e);
        }
    }

}
