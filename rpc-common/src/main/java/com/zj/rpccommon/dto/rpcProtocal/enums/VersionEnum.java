package com.zj.rpccommon.dto.rpcProtocal.enums;

/**
 * @author zhengjian
 * @date 2023-06-13 17:24
 */
public enum VersionEnum {

    VERSION_1((byte)1,"version1");

    byte version;

    String description;

    VersionEnum(byte version, String description) {
        this.version = version;
        this.description = description;
    }
}
