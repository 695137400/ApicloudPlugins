package com.apicloud.wifisyncserver;

import com.apicloud.commons.model.Config;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.intellij.openapi.util.io.FileUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-13-0013<br/>
 * Time: 17:27:39<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());
    private WebSocketServerHandshaker handshaker;
    private WifiSyncServer wifiSyncServer = null;
    private String projectName = null;

    private WebSocketServer webSocketServer = null;

    public WebSocketServerHandler(String name) {
        this.projectName = name;
        wifiSyncServer = RunProperties.getWifiSyncServer(name);
        webSocketServer = RunProperties.getWebSocketServer(name);
    }

    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            this.handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            this.handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }

    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (req.getDecoderResult().isSuccess() && "websocket".equals(req.headers().get("Upgrade"))) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8090/websocket", null, false);
            this.handshaker = wsFactory.newHandshaker(req);
            if (this.handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
            } else {
                String ip = getIPString(ctx);
                PrintUtil.info("新链接手机IP：" + ip, projectName);
                webSocketServer.getMap().put(ctx.channel(), ip);
                RunProperties.addIp(ip);
                this.handshaker.handshake(ctx.channel(), req);
                ctx.channel().write(new TextWebSocketFrame("{\"command\":\"7\",\"port\":\"" + webSocketServer.getHttpPort() + "\"}"));
            }

        } else {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            webSocketServer.getMap().remove(ctx.channel());
            this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
        } else if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        } else if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        } else {
            String request = ((TextWebSocketFrame) frame).text();
            String appID = null;
            String timeStamp = null;
            String commandID = null;

            String debugLevel;
            try {
                JSONObject json = new JSONObject(request);
                commandID = json.getString("command");
                if (commandID.equals("2")) {
                    appID = json.getString("appid");
                    timeStamp = json.getString("timestamp");
                } else {
                    if (commandID.equals("4")) {
                        ctx.channel().write(new TextWebSocketFrame(frame.content().retain()));
                        return;
                    }

                    if (commandID.equals("8")) {
                        debugLevel = json.getString("level");
                        String content = json.getString("content");
                        if (debugLevel.equals("normal")) {
                            PrintUtil.info("[WiFi log] " + content, projectName);
                        } else if (debugLevel.equals("error")) {
                            PrintUtil.info("[WiFi error] " + content, projectName);
                        } else if (debugLevel.equals("warn")) {
                            PrintUtil.info("[WiFi warning] " + content, projectName);
                        } else if (debugLevel.equals("debug")) {
                            PrintUtil.info("[WiFi debug] " + content, projectName);
                        } else {
                            PrintUtil.info("[WiFi info] " + content, projectName);
                        }

                        return;
                    }
                }
            } catch (JSONException var26) {
                var26.printStackTrace();
            }

            if (logger.isLoggable(Level.FINE)) {
                logger.fine(String.format("%s received %s", ctx.channel(), request));
            }

            if (appID != null) {
                debugLevel = wifiSyncServer.getWorkspacePath();
                if (!debugLevel.endsWith(File.separator)) {
                    debugLevel = debugLevel + File.separator;
                }

                ArrayList<String> newModifiedFileList = new ArrayList();
                File dirFile = new File(debugLevel);
                if (!dirFile.exists() || !dirFile.isDirectory()) {
                    return;
                }

                File[] files = dirFile.listFiles();
                String destPrjPath = null;
                File[] var16 = files;
                int size = files.length;

                File fileList;
                for (int var14 = 0; var14 < size; ++var14) {
                    fileList = var16[var14];
                    String id = "";
                    String path = "";
                    if (fileList.isFile()) {
                        if ("config.xml".equals(fileList.getName())) {
                            Config config = null;
                            try {
                                config = Config.loadXml(new FileInputStream(fileList));
                                id = config.getId();
                                path = fileList.getParent();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (fileList.isDirectory()) {
                        try {
                            path = fileList.getCanonicalPath();
                        } catch (IOException var25) {
                            var25.printStackTrace();
                        }

                        path.replaceAll("\\\\", "/");

                        File fileToRead = new File(path + File.separator + "config.xml");

                        try {
                            Config config = Config.loadXml(new FileInputStream(fileToRead));
                            id = config.getId();
                        } catch (FileNotFoundException var24) {
                            var24.printStackTrace();
                        }
                    }

                    if (id.equals(appID)) {
                        destPrjPath = path;
                        break;
                    }
                }

                if (destPrjPath != null) {
                    fileList = new File(FileUtil.getTempDirectory() + File.separator + appID + "_filelist.txt");
                    ArrayList<String> syncfilelist = new ArrayList();
                    String strJsonFormatted;
                    if (!fileList.exists()) {
                        try {
                            fileList.createNewFile();
                        } catch (IOException var23) {
                            var23.printStackTrace();
                        }
                    } else {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(fileList));
                            var16 = null;

                            while ((strJsonFormatted = reader.readLine()) != null) {
                                syncfilelist.add(strJsonFormatted);
                            }

                            reader.close();
                        } catch (FileNotFoundException var28) {
                            var28.printStackTrace();
                        } catch (IOException var29) {
                            var29.printStackTrace();
                        }
                    }

                    this.searchNewlyModifiedFiles(destPrjPath, newModifiedFileList, timeStamp, appID, syncfilelist);
                    size = newModifiedFileList.size();
                    PrintUtil.error("本次更新文件    " + size + "    个", projectName);
                    strJsonFormatted = "[";
                    if (size > 0) {
                        for (int i = 0; i < size - 1; ++i) {
                            strJsonFormatted = strJsonFormatted + "\"" + newModifiedFileList.get(i) + "\"" + ",";
                        }

                        strJsonFormatted = strJsonFormatted + "\"" + newModifiedFileList.get(size - 1) + "\"";
                    }

                    strJsonFormatted = strJsonFormatted + "]";

                    try {
                        JSONArray jsonarray = new JSONArray(strJsonFormatted);
                        long localTimestamp = System.currentTimeMillis();
                        ctx.channel().write(new TextWebSocketFrame("{\"command\":\"3\",\"timestamp\":\"" + localTimestamp + "\",\"list\":" + jsonarray.toString() + "}"));
                    } catch (JSONException var22) {
                        var22.printStackTrace();
                    }

                    try {
                        FileWriter fw = new FileWriter(fileList, false);
                        fw.write("");
                        fw.close();
                        if (syncfilelist.size() > 0) {
                            fw = new FileWriter(fileList, true);
                            PrintWriter out = new PrintWriter(fw);

                            for (int i = 0; i < syncfilelist.size(); ++i) {
                                out.write((String) syncfilelist.get(i));
                                out.println();
                            }

                            out.close();
                            fw.close();
                        }
                    } catch (IOException var27) {
                        var27.printStackTrace();
                    }

                    try {
                        fileList.createNewFile();
                    } catch (IOException var21) {
                        var21.printStackTrace();
                    }
                }
            } else {
                ctx.channel().write(new TextWebSocketFrame(request + " , 欢迎使用Netty WebSocket服务，现在时刻：" + (new Date()).toString()));
            }

        }
    }

    private void searchNewlyModifiedFiles(String dir, ArrayList<String> list, String timestamp, String appId, ArrayList<String> historyList) {
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }

        File dirFile = new File(dir);
        if (dirFile.exists() && dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            File[] var11 = files;
            int var10 = files.length;

            for (int var9 = 0; var9 < var10; ++var9) {
                File f = var11[var9];
                if (!f.getName().startsWith(".")) {
                    if (!f.isFile()) {
                        this.searchNewlyModifiedFiles(f.getAbsolutePath(), list, timestamp, appId, historyList);
                    } else {
                        String absolutePath = f.getAbsolutePath();
                        String workspaceDir = wifiSyncServer.getWorkspacePath();
                        //PrintUtil.info("absolutePath = " + absolutePath);
                        String relativePath;
                        if (!workspaceDir.endsWith("/") && !workspaceDir.endsWith("\\")) {
                            relativePath = absolutePath.substring(workspaceDir.length());
                        } else {
                            relativePath = absolutePath.substring(workspaceDir.length() - 1);
                        }

                        //PrintUtil.info("relativePath = " + relativePath);
                        String fString = relativePath.replaceAll("\\\\", "/");
                        int indexOf2ndSlash = fString.indexOf("/", 1);
                        String finalString = "/" + appId + fString.substring(indexOf2ndSlash);
                        if (historyList.size() <= 0) {
                            historyList.add(finalString);
                            list.add(finalString);
                            PrintUtil.info("更新----->" + finalString, projectName);
                        } else {
                            boolean synced = false;
                            Iterator var20 = historyList.iterator();

                            while (var20.hasNext()) {
                                String str = (String) var20.next();
                                if (str.equals(finalString)) {
                                    synced = true;
                                    break;
                                }
                            }

                            if (!synced) {
                                historyList.add(finalString);
                                list.add(finalString);
                                PrintUtil.info("更新----->" + finalString, projectName);
                            } else {
                                long fileModifyDate = f.lastModified();
                                long userTimeStamp = Long.valueOf(timestamp.replaceAll("[^0-9\\.]", ""));
                                if (fileModifyDate > userTimeStamp) {
                                    list.add(finalString);
                                    PrintUtil.info("更新----->" + finalString, projectName);
                                }
                            }
                        }
                    }
                }
            }

        } else {
            PrintUtil.info("Error:search " + dir + " failed, folder not exist!", projectName);
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, (long) res.content().readableBytes());
        }

        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public static String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }
}
