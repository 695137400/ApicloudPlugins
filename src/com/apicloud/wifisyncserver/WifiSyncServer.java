package com.apicloud.wifisyncserver;

import com.alibaba.fastjson.JSON;
import com.apicloud.plugin.run.ApicloudRun;
import com.apicloud.plugin.util.ApicloudConstant;
import com.apicloud.plugin.util.PrintUtil;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-7-0007<br/>
 * Time: 11:27:57<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class WifiSyncServer {

    private static String workspacePath;
    private static int httpport = 8080;
    private static int websocketport = 10915;
    static ApicloudRun httpServer = null;
    static ApicloudRun websocketServer = null;
    private static WebSocketServer socketServer = null;
    private static HttpFileServer fileServer = null;
    private static ApicloudRun http_runnable = null;
    private static ApicloudRun websocket_runnable = null;

    public WifiSyncServer() {
    }

    public static String getWorkspacePath() {
        return workspacePath;
    }

    public static void setWorkspacePath(String path) {
        workspacePath = path;
    }

    public static String getLocalIP() {
        StringBuilder sb = new StringBuilder();

        try {
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while (en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        sb.append(inetAddress.getHostAddress().toString() + ",");
                    }
                }
            }
        } catch (SocketException var5) {
            ;
        }

        return sb.toString();
    }

    public static void run(String[] args) throws Exception {
        if (args.length > 1) {
            setWorkspacePath(args[0]);
            System.setProperty("java.net.preferIPv4Stack", "true");
            String stop = args[1];
            PrintUtil.info(JSON.toJSONString(args));
            String laststr = "";
            if (null != ApicloudConstant.WIFI_CONFIG_INFO && "" != ApicloudConstant.WIFI_CONFIG_INFO) {
                laststr = ApicloudConstant.WIFI_CONFIG_INFO;
            }
            JSONObject jsonObject = new JSONObject(laststr);
            String ipstr = getLocalIP();
            jsonObject.put("ip", ipstr.substring(0, ipstr.length() - 1));
            ApicloudConstant.WIFI_CONFIG_INFO = jsonObject.toString();
            http_runnable = null;
            http_runnable = new ApicloudRun() {
                public void run() {
                    String url = "/";
                    System.out.println("http_runnable");
                    while (true) {
                        try {
                            fileServer = null;
                            fileServer = new HttpFileServer();
                            if (isPortUsing("127.0.0.1", httpport)) {
                                httpport += (new Random()).nextInt(10000);
                            } else {
                                fileServer.run(httpport, url);
                                break;
                            }
                        } catch (Exception var4) {
                            var4.printStackTrace();
                            break;
                        }
                    }

                }
            };
            if (httpServer != null) {
                fileServer.stop();
                http_runnable.stop();
                httpServer.stop();
                fileServer = null;
                http_runnable = null;
                httpServer = null;
            }
            if ("1".equals(stop)) {
                httpServer = new ApicloudRun(http_runnable);
                httpServer.start();
            }

            Thread.sleep(3000L);
            websocket_runnable = null;
            websocket_runnable = new ApicloudRun() {
                public void run() {
                    System.out.println("websocket_runnable");
                    while (true) {
                        try {
                            socketServer = null;
                            socketServer = new WebSocketServer();
                            if (isPortUsing("127.0.0.1", websocketport)) {
                                websocketport += (new Random()).nextInt(10000);
                            } else {
                                socketServer.run(websocketport);
                                break;
                            }
                        }  catch (Exception var3) {
                            var3.printStackTrace();
                            break;
                        }
                    }
                }
            };
            if (websocketServer != null) {
                socketServer.stop();
                websocket_runnable.stop();
                websocketServer.stop();
                socketServer = null;
                websocket_runnable = null;
                websocketServer = null;
            }
            if ("1".equals(stop)) {
                websocketServer = new ApicloudRun(websocket_runnable);
                websocketServer.start();
            }
        }
    }

    public static boolean isPortUsing(String host, int port) throws Exception {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static void main(String[] args) {
        try {
            System.out.println(isPortUsing("127.0.0.1", 8081));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
