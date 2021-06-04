package com.apicloud.plugin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apicloud.console.log.ConsoleLog;
import com.apicloud.plugin.run.IosLoader;
import com.apicloud.plugin.run.WebStorm;
import com.apicloud.wifisyncmanager.WifiSyncManager;
import com.apicloud.wifisyncserver.WebSocketServer;
import com.apicloud.wifisyncserver.WifiSyncServer;
import com.intellij.execution.ui.ConsoleView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: <br/>
 * Date: 18-10-31<br/>
 * Time: 下午2:09<br/>
 * Author:<br/>
 * Description: <span style="color:#63D3E9">运行时配置</span><br/>
 */
public class RunProperties {

    /**
     * WiFi运行是否开启
     */
    private static HashMap<String, Boolean> wifiRunMap = new HashMap<>();

    public static boolean wifiRun(String projectName) {
        if (null == wifiRunMap.get(projectName)) {
            wifiRun(projectName, false);
        }
        return wifiRunMap.get(projectName);
    }

    public static void wifiRun(String projectName, boolean isRun) {
        wifiRunMap.put(projectName, isRun);
    }

    private static HashMap<String, WifiSyncServer> wifiSyncServer = new HashMap<>();

    public static WifiSyncServer getWifiSyncServer(String name) {
        if (null == wifiSyncServer.get(name)) {
            wifiSyncServer.put(name, new WifiSyncServer(name));
        }
        return wifiSyncServer.get(name);
    }

    private static HashMap<String, WifiSyncManager> wifiSyncManager = new HashMap<>();

    public static WifiSyncManager getWifiSyncManager(String name) {
        if (null == wifiSyncManager.get(name)) {
            wifiSyncManager.put(name, new WifiSyncManager(name));
        }
        return wifiSyncManager.get(name);
    }

    private static HashMap<String, String> pushTypeMap = new HashMap<>();

    public static void pushType(String name, String type) {
        pushTypeMap.put(name, type);
    }

    public static String pushType(String name) {
        return pushTypeMap.get(name);
    }

    private static HashMap<String, ConsoleView> consoleView = new HashMap<>();

    public static ConsoleView console(String name) {
        return consoleView.get(name);
    }

    public static void console(String name, ConsoleView console) {
        consoleView.put(name, console);
    }

    private static HashMap<String, WebSocketServer> webSocketServer = new HashMap<>();

    public static WebSocketServer getWebSocketServer(String name) {
        if (null == webSocketServer.get(name)) {
            webSocketServer.put(name, new WebSocketServer(name));
        }
        return webSocketServer.get(name);
    }

    private static HashMap<String, WebStorm> webStorm = new HashMap<>();

    public static WebStorm getWebStorm(String name) {
        if (null == webStorm.get(name)) {
            webStorm.put(name, new WebStorm(name));
        }
        return webStorm.get(name);
    }

    private static HashMap<String, IosLoader> iosLoader = new HashMap<>();

    public static IosLoader getIosLoader(String name) {
        if (null == iosLoader.get(name)) {
            iosLoader.put(name, new IosLoader(name));
        }
        return iosLoader.get(name);
    }

    private static HashMap<String, ConsoleLog> consoleLog = new HashMap<>();

    public static ConsoleLog getConsoleLog(String name) {
        if (null == consoleLog.get(name)) {
            consoleLog.put(name, new ConsoleLog(name));
        }
        return consoleLog.get(name);
    }

    private static HashMap<String, String> adbList = new HashMap<>();

    public static String adbDevices(String name, String status) {
        if (null == adbList.get(name)) {
            adbList.put(name, status);
        }
        return adbList.get(name);
    }

    public static String adbDevices(String name) {
        return adbList.get(name);
    }

    private static HashMap<String, String> adbWifiList = new HashMap<>();

    public static String adbWifi(String name, String status) {
        adbWifiList.put(name, status);
        return adbWifiList.get(name);
    }

    public static String adbWifi(String name) {
        return adbWifiList.get(name);
    }

    private static HashMap<String, String> ipList = new HashMap<>();

    public static String adbIp(String ip, String status) {
        ipList.put(ip, status);
        return ipList.get(ip);
    }

    public static HashMap<String, String> getIpList() {
        return ipList;
    }

    private static ArrayList<String> ips = new ArrayList<>();

    public static ArrayList getIP() {
        return ips;
    }

    public static void addIp(String ip) {
        if (!ips.contains(ip)) {
            ips.add(ip);
            ipList.put(ip, "0");
        }
    }

    private static String adbPath = "";

    public static String getAdbPath() {
        return adbPath;
    }

    public static void setAdbPath(String adbPath) {
        RunProperties.adbPath = adbPath;
    }


    private static void adbTools(WebStorm webStorm, String projectName, String adbPath, String device) {
        String remote = "";
        String remotes[] = null;
        Object o = null;
        try {
            o = webStorm.runCmd(adbPath + " -s " + device + " shell cat /proc/net/unix |grep -a devtools_remote", false);
        } catch (Exception e) {

        }
        if (null != o) {
            remote = o.toString();
            remotes = remote.split("0000000000000000");
            remote = remotes[remotes.length - 1].substring(remotes[remotes.length - 1].indexOf("@") + 1);
        }
        if (null != remote && !"".equals(remote)) {
            try {
                o = webStorm.runCmd(adbPath + " -s " + device + "  forward tcp:9888 localabstract:" + remote, false);
                String s = HttpClientUtil.sendGet("http://localhost:9888/json", "v=1");
                if (null != s && !"".equals(s)) {
                    JSONArray array = (JSONArray) JSON.parse(s);
                    PrintUtil.info("USB同步完成，您可以将以下地址粘贴到谷歌浏览器进行手机调试", projectName);
                    String urls = "";
                    if (null != array) {
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jo = (JSONObject) array.get(i);
                            RunProperties.adbWifi(device + jo.getString("id"), "1");
                            PrintUtil.info("\n名称：" + jo.getString("title"), projectName);
                            PrintUtil.printUrl("url：", projectName, "chrome-devtools://devtools/bundled/inspector.html?ws=localhost:9888/devtools/page/" + jo.getString("id"));
                            PrintUtil.printInfoNoDate("说明： " + jo.getString("url"), projectName);

                        }
                        PrintUtil.info(urls + "\n", projectName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean wifiAdbConn = false;
    public static boolean pushRun = false;
    static String adbwwifi;

    private static void getDevices(WebStorm webStorm, String projectName) {
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("wifiAdb 启动");
                    while (wifiAdbConn) {
                        try {
                            Thread.sleep(3000);
                            adbwwifi = (String) webStorm.runCmd(adbPath + " devices", false);
                            System.out.println(adbwwifi);
                            String[] devicesList = adbwwifi.split("\n");
                            openTcp(devicesList, webStorm, projectName);
                        } catch (Exception e) {

                        }
                        if (pushRun) {
                            pushRun = false;
                            for (String ip : ipList.keySet()) {
                                try {
                                    adbTools(webStorm, projectName, getAdbPath(), ip + ":8888");
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                    System.out.println("wifiAdb 停止");
                } catch (Exception e) {

                }
            }
        }.start();
    }

    public static void wifiAdb(WebStorm webStorm, String projectName) {
        try {
            getDevices(webStorm, projectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openTcp(String[] devs, WebStorm webStorm, String projectName) {
        for (int i = 1; i < devs.length; i++) {
            String adbDevices = devs[i];
            if (null != adbDevices && !"".equals(adbDevices)) {
                String[] adb = adbDevices.split("\t");
                String name = adb[0];
                String status = adb[1];
                if (name.split("\\.").length > 2) {

                } else {
                    if ("device".equals(status)) {
                        if (null == adbList.get(name)) {
                            String tcpOut = (String) webStorm.runCmd(adbPath + " -s " + name + " tcpip 8888", false);
                            if (null != tcpOut && tcpOut.contains("restarting in TCP mode port")) {
                                adbList.put(name, "1");
                            }
                        }
                    } else {
                        adbList.remove(name);
                    }
                }
            }
        }
        for (String ip : ipList.keySet()) {
            String connTcpOut = (String) webStorm.runCmd(adbPath + " connect " + ip + ":8888", false);
            if (null != connTcpOut && !connTcpOut.contains("failed") && !connTcpOut.contains("refused")) {
                ipList.put(ip, "device");
            }
        }
    }


    public static String pluginsPath;
}
