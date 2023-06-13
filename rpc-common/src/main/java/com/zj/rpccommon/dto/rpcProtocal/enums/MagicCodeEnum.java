package com.zj.rpccommon.dto.rpcProtocal.enums;

/**
 * @author zhengjian
 * @date 2023-06-13 17:15
 */
public enum MagicCodeEnum {

    RPC_MESSAGE_MAGIC_CODE(1,"magicCodeVersion1");

    private int magicCodeEnum;

    private String description;

     MagicCodeEnum(int magicCodeEnum,String description){
        this.magicCodeEnum = magicCodeEnum;
        this.description = description;
    }

}
