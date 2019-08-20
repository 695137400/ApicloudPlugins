package com.apicloud.plugin.util;

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
}
