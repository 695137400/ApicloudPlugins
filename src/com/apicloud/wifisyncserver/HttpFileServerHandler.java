package com.apicloud.wifisyncserver;

import com.apicloud.commons.model.Config;
import com.apicloud.plugin.util.PrintUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-13-0013<br/>
 * Time: 17:35:35<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String url;
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
        } else if (request.getMethod() != HttpMethod.GET) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
        } else {
            String uri = request.getUri();
            String path = this.sanitizeUri(ctx, uri);
            if (path == null) {
                sendError(ctx, HttpResponseStatus.FORBIDDEN);
            } else if (path.equalsIgnoreCase("noop")) {
                sendError(ctx, HttpResponseStatus.OK);
            } else {
                File file = new File(path);
                if (!file.isHidden() && file.exists()) {
                    if (file.isDirectory()) {
                        if (uri.endsWith("/")) {
                            sendListing(ctx, file);
                        } else {
                            sendRedirect(ctx, uri + '/');
                        }

                    } else if (!file.isFile()) {
                        sendError(ctx, HttpResponseStatus.FORBIDDEN);
                    } else {
                        RandomAccessFile randomAccessFile = null;

                        try {
                            randomAccessFile = new RandomAccessFile(file, "r");
                        } catch (FileNotFoundException var12) {
                            sendError(ctx, HttpResponseStatus.NOT_FOUND);
                            return;
                        }

                        long fileLength = randomAccessFile.length();
                        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                        HttpHeaders.setContentLength(response, fileLength);
                        setContentTypeHeader(response, file);
                        if (HttpHeaders.isKeepAlive(request)) {
                            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                        }

                        ctx.write(response);
                        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile, 0L, fileLength, 8192), ctx.newProgressivePromise());
                        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                            }

                            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                            }
                        });
                        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                        if (!HttpHeaders.isKeepAlive(request)) {
                            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
                        }

                    }
                } else {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                }
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private String sanitizeUri(ChannelHandlerContext ctx, String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException var24) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException var23) {
                throw new Error();
            }
        }

        if (!uri.startsWith(this.url)) {
            return null;
        } else if (!uri.startsWith("/")) {
            return null;
        } else {
            int queryMarkIndex = uri.indexOf("?");
            String appID;
            String ipStrings;
            String destPrjPath;
            String result;
            if (queryMarkIndex != -1) {
                appID = uri.substring(queryMarkIndex + 1);
                if (appID.indexOf("&") == -1) {
                    if (appID.equalsIgnoreCase("action=ip")) {
                        ipStrings = WifiSyncServer.getLocalIP();
                        if (ipStrings.endsWith(",")) {
                            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(ipStrings.substring(0, ipStrings.length() - 1) + ":" + HttpFileServer.getWebsocketPort() + "\r\n", CharsetUtil.UTF_8));
                            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
                            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                            return "noop";
                        }
                    }
                } else {
                    String[] values = appID.split("&");
                    String fileurl;
                    Map map;
                    Map.Entry e;
                    Iterator var9;
                    Channel channel;
                    if (values[0].equalsIgnoreCase("action=sync") && values[1].startsWith("appid=")) {
                        fileurl = values[1].substring(6);
                        PrintUtil.info("[action=sync]appid = " + fileurl);
                        map = WebSocketServer.getMap();
                        if (map.size() < 1) {
                            PrintUtil.error("Error:没有手机连接到WiFi同步服务.\n请先到apploader或自定义loader中配置WiFi同步参数。");
                            return "noop";
                        }

                        var9 = map.entrySet().iterator();

                        while(var9.hasNext()) {
                            e = (Map.Entry)var9.next();
                            channel = (Channel)e.getKey();
                            channel.writeAndFlush(new TextWebSocketFrame("{\"command\":\"1\",\"appid\":\"" + fileurl + "\",\"updateAll\":false" + "}"));
                        }

                        return "noop";
                    }

                    if (values[0].equalsIgnoreCase("action=review") && values[1].startsWith("path=")) {
                        fileurl = values[1].substring(5);
                        fileurl = fileurl.substring(1, fileurl.length() - 1);
                        PrintUtil.info("[action=review]path = " + fileurl);
                        if (!fileurl.endsWith("htm") && !fileurl.endsWith("html")) {
                            return "noop";
                        }

                        fileurl = fileurl.replaceAll("\\\\", "/");
                        String parentPath = WifiSyncServer.getWorkspacePath();
                        parentPath = parentPath.replaceAll("\\\\", "/");
                        if (parentPath.endsWith("/")) {
                            parentPath = parentPath.substring(0, parentPath.length() - 1);
                        }

                        File dirFile = new File(parentPath);
                        if (!dirFile.exists() || !dirFile.isDirectory()) {
                            return "noop";
                        }

                        File[] files = dirFile.listFiles();
                        destPrjPath = null;
                        File[] var14 = files;
                        int var13 = files.length;

                        for(int var12 = 0; var12 < var13; ++var12) {
                            File f = var14[var12];
                            if (f.isDirectory()) {
                                String path = "";

                                try {
                                    path = f.getCanonicalPath();
                                } catch (IOException var22) {
                                    var22.printStackTrace();
                                }

                                path = path.replaceAll("\\\\", "/");
                                if (fileurl.contains(path) && fileurl.substring(path.length()).startsWith("/")) {
                                    destPrjPath = path;
                                    break;
                                }
                            }
                        }

                        if (destPrjPath != null) {
                            String id = "";
                            File fileToRead = new File(destPrjPath + File.separator + "config.xml");

                            try {
                                Config config = Config.loadXml(new FileInputStream(fileToRead));
                                id = config.getId();
                            } catch (FileNotFoundException var18) {
                                var18.printStackTrace();
                            }

                            result = "/" + id + fileurl.substring(destPrjPath.length());
                            Map<Channel, String> map2 = WebSocketServer.getMap();
                            if (map2.size() < 1) {
                                PrintUtil.error("Error:没有手机连接到WiFi同步服务.\n请先到apploader或自定义loader中配置WiFi同步参数。");
                                return "noop";
                            }

                            Iterator var16 = map2.entrySet().iterator();
                            while(var16.hasNext()) {
                                e = (Map.Entry)var16.next();
                                channel = (Channel)e.getKey();
                                channel.writeAndFlush(new TextWebSocketFrame("{\"command\":\"6\",\"path\":\"" + result + "\",\"appid\":\"" + id + "\"}"));
                            }

                            return "noop";
                        }
                    } else {
                        if (values[0].equalsIgnoreCase("action=workspace") && values[1].startsWith("path=")) {
                            fileurl = values[1].substring(5);
                            fileurl = fileurl.substring(1, fileurl.length() - 1);
                            PrintUtil.info("[action=workspace]path = " + fileurl);
                            WifiSyncServer.setWorkspacePath(fileurl);
                            return "noop";
                        }

                        if (values[0].equalsIgnoreCase("action=syncall") && values[1].startsWith("appid=")) {
                            fileurl = values[1].substring(6);
                            PrintUtil.info("[action=syncall]appid = " + fileurl);
                            map = WebSocketServer.getMap();
                            if (map.size() < 1) {
                                PrintUtil.error("Error:没有手机连接到WiFi同步服务.\n请先到apploader或自定义loader中配置WiFi同步参数。");
                                return "noop";
                            }

                            var9 = map.entrySet().iterator();

                            while(var9.hasNext()) {
                                e = (Map.Entry)var9.next();
                                channel = (Channel)e.getKey();
                                channel.writeAndFlush(new TextWebSocketFrame("{\"command\":\"1\",\"appid\":\"" + fileurl + "\",\"updateAll\":true" + "}"));
                            }

                            return "noop";
                        }
                    }
                }
            }

            uri = uri.replaceAll("\\?.*", "");
            appID = null;
            if (uri.indexOf("/", 1) > 1) {
                appID = uri.substring(1, uri.indexOf("/", 1));
                ipStrings = WifiSyncServer.getWorkspacePath();
                if (!ipStrings.endsWith(File.separator)) {
                    ipStrings = ipStrings + File.separator;
                }

                File dirFile = new File(ipStrings);
                if (dirFile.exists() && dirFile.isDirectory()) {
                    File[] files = dirFile.listFiles();
                    destPrjPath = null;
                    File[] var40 = files;
                    int var38 = files.length;

                    File f;
                    for(int var37 = 0; var37 < var38; ++var37) {
                        f = var40[var37];
                        if (f.isDirectory()) {
                            result = "";

                            try {
                                result = f.getCanonicalPath();
                            } catch (IOException var21) {
                                var21.printStackTrace();
                            }

                            result.replaceAll("\\\\", "/");
                            String id = "";
                            File fileToRead = new File(result + File.separator + "config.xml");

                            try {
                                Config config = Config.loadXml(new FileInputStream(fileToRead));
                                id = config.getId();
                            } catch (FileNotFoundException var20) {
                                var20.printStackTrace();
                            }

                            if (id.equals(appID)) {
                                destPrjPath = result;
                                break;
                            }
                        }
                    }

                    if (destPrjPath != null) {
                        f = new File(destPrjPath);
                        destPrjPath = f.getName();
                        uri = "/" + destPrjPath + uri.substring(uri.indexOf("/", 1));
                    }

                    uri = uri.replace('/', File.separatorChar);
                    if (!uri.contains(File.separator + '.') && !uri.contains('.' + File.separator) && !uri.startsWith(".") && !uri.endsWith(".") && !INSECURE_URI.matcher(uri).matches()) {
                        result = null;

                        try {
                            result = dirFile.getCanonicalPath().replaceAll("\\\\", "/") + uri;
                        } catch (IOException var19) {
                            var19.printStackTrace();
                        }

                        return result;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private static void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append(" 目录：");
        buf.append("</title></head><body>\r\n");
        buf.append("<h3>");
        buf.append(dirPath).append(" 目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
        File[] var8;
        int var7 = (var8 = dir.listFiles()).length;

        for(int var6 = 0; var6 < var7; ++var6) {
            File f = var8[var6];
            if (!f.isHidden() && f.canRead()) {
                String name = f.getName();
                if (ALLOWED_FILE_NAME.matcher(name).matches()) {
                    buf.append("<li>链接：<a href=\"");
                    buf.append(name);
                    buf.append("\">");
                    buf.append(name);
                    buf.append("</a></li>\r\n");
                }
            }
        }

        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        mimeTypesMap.addMimeTypes("text/css css");
        mimeTypesMap.addMimeTypes("application/x-javascript js");
        mimeTypesMap.addMimeTypes("image/svg+xml svg");
        mimeTypesMap.addMimeTypes("text/html htm");
        mimeTypesMap.addMimeTypes("text/html html");
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }
}
