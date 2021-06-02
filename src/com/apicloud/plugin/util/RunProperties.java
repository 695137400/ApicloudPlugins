package com.apicloud.plugin.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apicloud.console.log.ConsoleLog;
import com.apicloud.plugin.run.IosLoader;
import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.ui.createApp.CreateAppFrom;
import com.apicloud.wifisyncmanager.WifiSyncManager;
import com.apicloud.wifisyncserver.WebSocketServer;
import com.apicloud.wifisyncserver.WifiSyncServer;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
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

    public static String adbIp(String ip) {
        return ipList.get(ip);
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


    public static Thread adbThread;



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
                            PrintUtil.info("\n名称：" + new String(jo.getString("title").getBytes("gbk"), "utf-8"), projectName);
                            PrintUtil.printUrl("url：", projectName, "chrome-devtools://devtools/bundled/inspector.html?ws=localhost:9888/devtools/page/" + jo.getString("id"));
                            PrintUtil.printInfoNoDate("说明： " + jo.getString("url"), projectName);

                        }
                        PrintUtil.info(urls + "\n", projectName);
                        Thread runnable = new Thread() {
                            public void run() {
                                ConsoleLog consoleLog = RunProperties.getConsoleLog(projectName);
                                consoleLog.main(adbPath + "/", device);
                            }
                        };
                        runnable.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void wifiAdb(WebStorm webStorm, String projectName, String adbPath) {
        Object adbwwifi = null;
        try {
            String adbList[] = new String[0];
            try {
                adbwwifi = webStorm.runCmd(adbPath + " devices", false);
                System.out.println(adbwwifi);
                adbList = adbwwifi.toString().split("\n");
            } catch (Exception e) {

            }
            if (null != adbList && adbList.length > 0) {
                for (int i = 1; i < adbList.length; i++) {
                    final String adbDevices = adbList[i];
                    if (null != adbDevices && !"".equals(adbDevices)) {
                        String[] adb = adbDevices.split("\t");
                        String name = adb[0];
                        String status = adb[1];
                        if (name.split("\\.").length > 2) {// ip
                            RunProperties.adbIp(name, status);
                            String s = null;
                            try {
                                s = HttpClientUtil.sendGet("http://localhost:9888/json", "v=1");
                            } catch (Exception e) {

                            }
                            if (null == s || "".equals(s)) {
                                adbTools(webStorm, projectName, adbPath, name);
                            } else {
                                JSONArray array = (JSONArray) JSON.parse(s);
                                String urls = "";
                                if (null != array) {
                                    boolean isnew = false;
                                    for (int y = 0; y < array.size(); y++) {
                                        JSONObject jo = (JSONObject) array.get(y);
                                        if (null == RunProperties.adbWifi(name + jo.getString("id"))) {
                                            if (!isnew) {
                                                PrintUtil.info("\n调试地址更新：" + urls + "\n", projectName);
                                                isnew = true;
                                            }
                                            RunProperties.adbWifi(name + jo.getString("id"), "1");
                                            PrintUtil.info("\n名称：" + new String(jo.getString("title").getBytes("gbk"), "utf-8"), projectName);
                                            PrintUtil.printUrl("url：", projectName, "chrome-devtools://devtools/bundled/inspector.html?ws=localhost:9888/devtools/page/" + jo.getString("id"));
                                            PrintUtil.printInfoNoDate("说明： " + jo.getString("url"), projectName);
                                        }
                                    }
                                }
                            }
                        } else {//编号
                            RunProperties.adbDevices(name, status);
                            if ("device".equals(status) || (null != RunProperties.adbDevices(name) && !"device".equals(RunProperties.adbDevices(name)))) {
                                try {
                                    webStorm.runCmd(adbPath + " tcpip 8888", false);
                                    ArrayList<String> ips = RunProperties.getIP();
                                    if (ips.size() > 0) {
                                        for (int p = 0; p < ips.size(); p++) {
                                            try {
                                                String ipstatus = RunProperties.adbIp(ips.get(p));
                                                if (null == ipstatus || (null != ipstatus && !"device".equals(ipstatus))) {
                                                    webStorm.runCmd(adbPath + " connect " + ips.get(p) + ":8888", false);
                                                    RunProperties.adbIp(ips.get(p), "1");
                                                    PrintUtil.error("检测到有已链接的USB设备，开启可用 WIFI ADB 无线调试，您现在可以断开USB数据链接，打开谷歌浏览器进行方便的无线调试", projectName);
                                                    adbTools(webStorm, projectName, adbPath, name);
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

    }
}
