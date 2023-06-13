package com.zj.rpccommon.dto.rpcProtocal.enums;

/**
 * @author zhengjian
 * @date 2023-06-13 17:47
 */
public enum CompressTypeEnum {

        GZIP((byte)1,"gzip");

        private byte compressTypeNum;

        private String description;


    public byte getCompressTypeNum() {
        return compressTypeNum;
    }

    public void setCompressTypeNum(byte compressTypeNum) {
        this.compressTypeNum = compressTypeNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    CompressTypeEnum(byte compressTypeNum, String description) {
            this.compressTypeNum = compressTypeNum;
            this.description = description;
        }
}
