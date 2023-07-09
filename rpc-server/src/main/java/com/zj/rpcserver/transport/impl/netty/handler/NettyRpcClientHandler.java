package com.zj.rpcserver.transport.impl.netty.handler;

import com.zj.rpccommon.dto.RpcResponse;
import com.zj.rpccommon.dto.rpcProtocal.RpcMessage;
import com.zj.rpccommon.dto.rpcProtocal.enums.CompressTypeEnum;
import com.zj.rpccommon.dto.rpcProtocal.enums.MessageTypeEnum;
import com.zj.rpccommon.dto.rpcProtocal.enums.RpcConstants;
import com.zj.rpccommon.dto.rpcProtocal.enums.SerializeTypeEnum;
import com.zj.rpcserver.transport.impl.netty.NettyRpcClient;
import com.zj.rpcserver.transport.impl.netty.UnprocessedRequests;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author zhengjian
 * @date 2023-07-08 21:19
 */
@Slf4j
@Component
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private UnprocessedRequests unprocessedRequests;


    @Autowired
    private NettyRpcClient nettyRpcClient;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.info("client receive from server,mes=[{}]", msg);
            if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                byte messageType = rpcMessage.getMessageType();
                if (messageType == MessageTypeEnum.HEARTBEAT_RESPONSE.getMessageTypeNum()) {
                    log.info("client receive the pong:[{}]", rpcMessage.getBody());
                } else if (messageType == MessageTypeEnum.RPC_RESPONSE.getMessageTypeNum()) {
                    RpcResponse<Object> rpcResponse = (RpcResponse<Object>) rpcMessage.getBody();
                    unprocessedRequests.complete(rpcResponse);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state==IdleState.WRITER_IDLE){
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                //根据ip地址获取对应的通道
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = new RpcMessage();
                rpcMessage.setSerializeType(SerializeTypeEnum.JDK.getSerializeType());
                rpcMessage.setCompressType(CompressTypeEnum.GZIP.getCompressType());
                rpcMessage.setMessageType(MessageTypeEnum.HEARTBEAT_REQUEST.getMessageTypeNum());
                rpcMessage.setBody(RpcConstants.PING);
                //如果在通道传输过程中，发现有相关的失败操作，那么会关闭该通道
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
