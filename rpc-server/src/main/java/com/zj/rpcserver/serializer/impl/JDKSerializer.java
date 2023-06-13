package com.zj.rpcserver.serializer.impl;

import com.zj.rpccommon.exception.SerializeException;
import com.zj.rpcserver.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author zhengjian
 * @date 2023-06-09 18:55
 */
public class JDKSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream)){
            oos.writeObject(obj);
            oos.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializeException("JDKSerializer failed:"+e.getMessage());
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream)) {
            Object o = ois.readObject();
            return clazz.cast(o);
        } catch (Exception e){
            throw new SerializeException("JDKDeSerialize failed:"+e.getMessage());
        }
    }
}
