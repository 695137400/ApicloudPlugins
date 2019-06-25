//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.apicloud.wifisyncserver;

import com.apicloud.plugin.util.RunProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
    private static String websocketPort = null;
    private Channel ch = null;
    private String name = null;
    private WebSocketServer webSocketServer = null;

    public HttpFileServer(String name) {
        this.name = name;
        webSocketServer = RunProperties.getWebSocketServer(name);
    }

    public static String getWebsocketPort() {
        return websocketPort;
    }

    public static void setWebsocketPort(String port) {
        websocketPort = port;
    }

    public void run(int port, final String url) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url, name));
                }
            });
            ChannelFuture future = b.bind(port).sync();
            webSocketServer.setHttpPort("" + port);
            ch = future.channel();
            ch.closeFuture().sync();
        } catch (Exception e) {

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
