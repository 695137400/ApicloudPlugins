package com.apicloud.wifisyncserver;

import com.alibaba.fastjson.JSON;
import com.apicloud.plugin.run.ApicloudRun;
import com.apicloud.plugin.util.ApicloudConstant;
import com.apicloud.plugin.util.PrintUtil;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
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

    private String workspacePath;
    private int httpport = 8080;
    private int websocketport = 10915;
    ApicloudRun httpServer = null;
    ApicloudRun websocketServer = null;
    private WebSocketServer socketServer = null;
    private HttpFileServer fileServer = null;
    private ApicloudRun http_runnable = null;
    private ApicloudRun websocket_runnable = null;
    private String projectName = null;

    public WifiSyncServer(String name) {
        this.projectName = name;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String path) {
        workspacePath = path;
    }

    public String getLocalIP() {
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

    public void run(String[] args) throws Exception {
        if (args.length > 1) {
            setWorkspacePath(args[0]);
            System.setProperty("java.net.preferIPv4Stack", "true");
            String stop = args[1];
            PrintUtil.info(JSON.toJSONString(args), projectName);
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
                            fileServer = new HttpFileServer(projectName);
                            if (isPortUsing("127.0.0.1", httpport)) {
                                httpport += (new Random()).nextInt(10000);
                            } else {
                                fileServer.run(httpport, url);
                                jsonObject.put("http_port", httpport);
                                ApicloudConstant.WIFI_CONFIG_INFO = jsonObject.toString();
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
                if (null != fileServer) {
                    fileServer.stop();
                }
                if (null != http_runnable) {
                    http_runnable.stop();
                }
                if (null != httpServer) {
                    httpServer.stop();
                }
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
                            socketServer = new WebSocketServer(projectName);
                            if (isPortUsing("127.0.0.1", websocketport)) {
                                websocketport += (new Random()).nextInt(10000);
                            } else {
                                socketServer.run(websocketport);
                                jsonObject.put("http_port", httpport);
                                ApicloudConstant.WIFI_CONFIG_INFO = jsonObject.toString();
                                break;
                            }
                        } catch (Exception var3) {
                            var3.printStackTrace();
                            break;
                        }
                    }
                }
            };
            if (websocketServer != null) {
                if (null != socketServer) {
                    socketServer.stop();
                }
                if (null != websocket_runnable) {
                    websocket_runnable.stop();
                }
                if (null != websocketServer) {
                    websocketServer.stop();
                }
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

    private boolean isPortUsing(String host, int port) throws Exception {
        boolean flag = false;
        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(theAddress, port);
            flag = true;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return flag;
    }


}
