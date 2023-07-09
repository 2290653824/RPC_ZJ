package com.zj.rpcserver.transport.impl.netty;

import com.zj.rpccommon.dto.RpcRequest;
import com.zj.rpccommon.dto.RpcResponse;
import com.zj.rpccommon.dto.rpcProtocal.RpcMessage;
import com.zj.rpccommon.dto.rpcProtocal.enums.CompressTypeEnum;
import com.zj.rpccommon.dto.rpcProtocal.enums.MessageTypeEnum;
import com.zj.rpccommon.dto.rpcProtocal.enums.SerializeTypeEnum;
import com.zj.rpcserver.register.ServiceRegister;
import com.zj.rpcserver.transport.RpcRequestTransport;
import com.zj.rpcserver.transport.impl.netty.codec.RpcMessageDecoder;
import com.zj.rpcserver.transport.impl.netty.codec.RpcMessageEncoder;
import com.zj.rpcserver.transport.impl.netty.handler.NettyRpcClientHandler;
import com.zj.rpcserver.transport.impl.netty.provider.ChannelProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * initialize and close Bootstrap object
 *
 * @author shuang.kou
 * @createTime 2020年05月29日 17:51:00
 */
@Slf4j
@Component
public final class NettyRpcClient implements RpcRequestTransport {
    @Resource(name = "zookeeperServiceRegister")
    private  ServiceRegister serviceRegister;
    @Resource
    private NettyRpcClientHandler nettyRpcClientHandler;
    @Resource
    private  UnprocessedRequests unprocessedRequests;
    @Resource
    private ChannelProvider channelProvider;
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;


    @PostConstruct
    public void init() {
        // initialize resources such as EventLoopGroup, Bootstrap
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //  The timeout period of the connection.
                //  If this time is exceeded or the connection cannot be established, the connection fails.
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(nettyRpcClientHandler);
                    }
                });
    }

    /**
     * connect server and get the channel ,so that you can send rpc message to server
     *
     * @param inetSocketAddress server address
     * @return the channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }


    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.getChannel(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.addChannel(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        // build return value
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // get server address
        InetSocketAddress inetSocketAddress = serviceRegister.serviceDiscovery(rpcRequest.getRpcServiceName());
        // get  server address related channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // put unprocessed request
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder().body(rpcRequest)
                    .serializeType(SerializeTypeEnum.JDK.getSerializeType())
                    .compressType(CompressTypeEnum.GZIP.getCompressType())
                    .messageType(MessageTypeEnum.RPC_REQUEST.getMessageTypeNum()).build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }
}
