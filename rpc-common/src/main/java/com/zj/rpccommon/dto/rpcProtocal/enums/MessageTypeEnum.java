package com.zj.rpccommon.dto.rpcProtocal.enums;

/**
 * @author zhengjian
 * @date 2023-06-13 17:40
 */
public enum MessageTypeEnum {

        RPC_MESSAGE((byte)1,"rpcMessage");

        private byte messageTypeNum;

        private String description;

        MessageTypeEnum(byte messageTypeNum, String description) {
            this.messageTypeNum = messageTypeNum;
            this.description = description;
        }


}
