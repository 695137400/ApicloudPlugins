package com.apicloud.wifisyncmanager;

import com.apicloud.networkservice.ConnectionUtil;
import com.apicloud.plugin.util.ApicloudConstant;
import com.apicloud.plugin.util.FileUtil;
import com.apicloud.plugin.util.PrintUtil;
import com.intellij.openapi.project.Project;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-7-0007<br/>
 * Time: 9:37:02<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class WifiSyncManager {

    private static String wigetPath;
    private String projectName = null;

    public WifiSyncManager(String name) {
        this.projectName = name;
    }

    public void main(String[] args) throws Exception {
        wigetPath = "";
        if (args.length > 0) {
            int commandID = Integer.valueOf(args[0].replaceAll("[^0-9\\.]", ""));
            String laststr = "";
            if (null != ApicloudConstant.WIFI_CONFIG_INFO && "" != ApicloudConstant.WIFI_CONFIG_INFO) {
                laststr = ApicloudConstant.WIFI_CONFIG_INFO;
            }
            JSONObject jsonObject = new JSONObject(laststr);
            String httpPort = jsonObject.getString("http_port");
            String htmlPath;
            String workspacePath;
            String message0;
            String configPath;
            String appId;
            switch (commandID) {
                case 1:
                    htmlPath = "http://127.0.0.1:" + httpPort + "/?action=ip";
                    workspacePath = ConnectionUtil.getConnectionMessage(htmlPath, null);
                    String[] values = workspacePath.split(":");
                    String[] ipValues = values[0].split(",");
                    PrintUtil.info("WiFi 端口: " + values[1], projectName);
                    int i = 0;
                    String[] var33 = ipValues;
                    int var31 = ipValues.length;

                    for (int var32 = 0; var32 < var31; ++var32) {
                        appId = var33[var32];
                        PrintUtil.info("WiFi 链接地址( " + i + "): " + appId, projectName);
                        ++i;
                    }

                    return;
                case 2:
                case 3:
                    if (args[1] != null) {
                        wigetPath = args[1];
                    }

                    if (args[2] == null) {
                        return;
                    }

                    htmlPath = args[2];
                    workspacePath = "http://127.0.0.1:" + httpPort + "/?action=workspace" + "&path=\"" + htmlPath + "\"";
                    PrintUtil.info("连接到：" + workspacePath, projectName);
                    message0 = ConnectionUtil.getConnectionMessage(workspacePath, null);
                    PrintUtil.info("message = " + message0, projectName);
                    getWidgetPath(wigetPath);
                    PrintUtil.info("wigetPath = " + wigetPath, projectName);
                    configPath = wigetPath + File.separator + "config.xml";
                    PrintUtil.info("configPath = " + configPath, projectName);
                    File var29 = new File(configPath);
                    if (!var29.exists()) {
                        PrintUtil.info("Not found config.xml", projectName);
                        return;
                    }
                    PrintUtil.info("getAppId...", projectName);
                    appId = FileUtil.getAppId(configPath);
                    if (appId != null && !"".equals(appId)) {
                        String cmd = null;
                        if (commandID == 2) {
                            cmd = "syncall";
                        } else {
                            cmd = "sync";
                        }

                        String url = "http://127.0.0.1:" + httpPort + "/?action=" + cmd + "&appid=" + appId;
                        PrintUtil.info("url = " + url, projectName);
                        String message = ConnectionUtil.getConnectionMessage(url, null);
                        PrintUtil.info("message = " + message, projectName);
                        break;
                    }
                    PrintUtil.info("Please make sure the directory is correct", projectName);
                    return;
                case 4:
                    htmlPath = null;
                    if (args[1] != null) {
                        htmlPath = args[1];
                    }

                    workspacePath = null;
                    if (args[2] == null) {
                        return;
                    }

                    workspacePath = args[2];
                    message0 = "http://127.0.0.1:" + httpPort + "/?action=workspace" + "&path=\"" + workspacePath + "\"";
                    configPath = ConnectionUtil.getConnectionMessage(message0, null);
                    PrintUtil.info("message = " + configPath, projectName);
                    String var12 = "http://127.0.0.1:" + httpPort + "/?action=review" + "&path=\"" + htmlPath + "\"";
                    appId = ConnectionUtil.getConnectionMessage(var12, null);
                    PrintUtil.info("message = " + appId, projectName);
            }

        }
    }


    private void getWidgetPath(String filePath) {
        if (filePath != null && !"".equals(filePath)) {
            File file = new File(filePath);
            String[] fileList = file.list();
            boolean isContinue = true;
            String[] var7 = fileList;
            int var6 = fileList.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                String fileS = var7[var5];
                if ("config.xml".equals(fileS)) {
                    String content = FileUtil.getWidgetContent(filePath + File.separator + "config.xml");
                    if (isContent(fileList, content)) {
                        wigetPath = filePath;
                        isContinue = false;
                        break;
                    }
                }
            }

            if (isContinue) {
                getWidgetPath(file.getParent());
            }

        }
    }


    private boolean isContent(String[] listFile, String content) {
        String[] var5 = listFile;
        int var4 = listFile.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            String file = var5[var3];
            if (file.equals(content)) {
                return true;
            }
        }

        return false;
    }

}
