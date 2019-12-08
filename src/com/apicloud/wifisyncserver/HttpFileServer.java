//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.wifisyncserver;

import com.apicloud.plugin.util.RunProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.SocketAddress;

public class HttpFileServer {
    private Channel ch = null;
    private String name = null;
    private WebSocketServer webSocketServer = null;

    public HttpFileServer(String name) {
        this.name = name;
        webSocketServer = RunProperties.getWebSocketServer(name);
    }

    public void run(int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            ServerBootstrap channel = b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
            ChannelInitializer<SocketChannel> fileServerHandler = new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast("http-decoder", new HttpRequestDecoder());
                    pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
                    pipeline.addLast("http-encoder", new HttpResponseEncoder());
                    pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast("fileServerHandler", new HttpFileServerHandler(url, name));
                    System.out.println("HttpFileServer------------------initChannel");
                }
            };
            channel.childHandler(fileServerHandler);
            ChannelFuture future = b.bind(port).sync();

            ch = future.channel();
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public void stop() {
        ch.disconnect();
        ch.close();
        ch = null;
    }
}
