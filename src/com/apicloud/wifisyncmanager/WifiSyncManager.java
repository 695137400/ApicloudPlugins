package com.apicloud.wifisyncmanager;

import com.alibaba.fastjson.JSON;
import com.apicloud.networkservice.ConnectionUtil;
import com.apicloud.plugin.run.WifiQrcode;
import com.apicloud.plugin.util.ApicloudConstant;
import com.apicloud.plugin.util.FileUtil;
import com.apicloud.plugin.util.PrintUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.intellij.openapi.project.Project;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

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
                    try {
                        Map<String, Object> wifiObj = new HashMap<>();
                        wifiObj.put("port", values[1]);
                        ArrayList ips = new ArrayList();
                        ips.add(var33[var33.length - 1]);
                        wifiObj.put("ips", ips);
                        System.out.println(JSON.toJSON(wifiObj));

                        QRCodeWriter qrCodeWriter1 = new QRCodeWriter();
                        //设置二维码图片宽高
                        BitMatrix bitMatrix = qrCodeWriter1.encode(JSON.toJSONString(wifiObj), BarcodeFormat.QR_CODE, 300, 300);
                        int width = bitMatrix.getWidth();
                        int height = bitMatrix.getHeight();

                        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                        for (int q = 0; q < width; q++) {
                            for (int j = 0; j < height; j++) {
                                image.setRGB(q, j, bitMatrix.get(q, j) ? 0XFF000000 : 0XFFFFFF);
                            }
                        }
                         WifiQrcode.getInstance().setIcon(image);
                    } catch (WriterException e) {
                        e.printStackTrace();
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
                    message0 = ConnectionUtil.getConnectionMessage(workspacePath, null);
                    getWidgetPath(wigetPath);
                    configPath = wigetPath + File.separator + "config.xml";
                    File var29 = new File(configPath);
                    if (!var29.exists()) {
                        PrintUtil.info("Not found config.xml", projectName);
                        return;
                    }
                    appId = FileUtil.getAppId(configPath);
                    if (appId != null && !"".equals(appId)) {
                        String cmd = null;
                        if (commandID == 2) {
                            cmd = "syncall";
                        } else {
                            cmd = "sync";
                        }

                        String url = "http://127.0.0.1:" + httpPort + "/?action=" + cmd + "&appid=" + appId;
                        String message = ConnectionUtil.getConnectionMessage(url, null);
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
                    String var12 = "http://127.0.0.1:" + httpPort + "/?action=review" + "&path=\"" + htmlPath + "\"";
                    appId = ConnectionUtil.getConnectionMessage(var12, null);
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
