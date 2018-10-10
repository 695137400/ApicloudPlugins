package com.apicloud.wifisyncserver;

import com.apicloud.plugin.util.ApicloudConstant;
import com.apicloud.plugin.util.PrintUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.json.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-13-0013<br/>
 * Time: 17:34:19<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class WebSocketServer {

    private static Map<Channel, String> map = new ConcurrentHashMap();
    private static String httpPort = "00";
    private Channel ch = null;

    public WebSocketServer() {
    }

    public static Map<Channel, String> getMap() {
        return map;
    }

    public static synchronized String getHttpPort() {
        return httpPort;
    }

    public static synchronized void setHttpPort(String port) {
        httpPort = port;
    }

    public void run(int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("http-codec", new HttpServerCodec());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    pipeline.addLast("handler", new WebSocketServerHandler());
                }
            });
            ch = b.bind(port).sync().channel();
            PrintUtil.info("WiFi 端口更新: " + port);

            String laststr = "";
            if (null != ApicloudConstant.WIFI_CONFIG_INFO && "" != ApicloudConstant.WIFI_CONFIG_INFO) {
                laststr = ApicloudConstant.WIFI_CONFIG_INFO;
            }

            JSONObject jsonObject = new JSONObject(laststr);
            jsonObject.put("websocket_port", "" + port);
            jsonObject.put("http_port", getHttpPort());

            ApicloudConstant.WIFI_CONFIG_INFO = jsonObject.toString();
            HttpFileServer.setWebsocketPort("" + port);
            ch.closeFuture().syncUninterruptibly();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public void stop() {
        ch.disconnect();
        ch.close();
        ch = null;
        map.clear();
    }
}
