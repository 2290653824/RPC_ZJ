package com.zj.rpccommon.dto.rpcProtocal.enums;

/**
 * @author zhengjian
 * @date 2023-06-13 17:44
 */

public enum SerializeTypeEnum {

        HESSIAN((byte)1,"hessian"),
        PROTOSTUFF((byte)2,"protostuff"),
        KRYO((byte)3,"kryo"),

        JDK((byte)4,"jdk");

        private byte serializeType;

        private String description;

        SerializeTypeEnum(byte serializeType, String description) {
            this.serializeType = serializeType;
            this.description = description;
        }

    public byte getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(byte serializeType) {
        this.serializeType = serializeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
