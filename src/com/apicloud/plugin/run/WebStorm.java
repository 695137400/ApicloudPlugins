/**
 * WebStorm APICloud plugin
 * Copyright (c) 2014-2015 by APICloud, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3.
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.apicloud.plugin.run;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.apicloud.plugin.util.FileUtil;
import com.apicloud.plugin.util.PrintUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebStorm {
    private String OS = System.getProperty("os.name").toLowerCase();
    private String pkgName;
    private String loaderName;
    private String pendingVersion;
    private String workPath;//ProjectFileDir
    public String adbPath;//
    private String cmdLogType;//load.conf
    private String wigetPath;
    private String projectName;

    public WebStorm(String name) {
        projectName = name;
    }

    public void run(String workPathA, String wigetPathA) {
        workPath = workPathA;
        wigetPath = wigetPathA;
        PrintUtil.info("workPath:" + workPath, projectName);
        PrintUtil.info("wigetPath:" + wigetPath, projectName);
        PrintUtil.info("start...", projectName);
        getWidgetPath(wigetPath);

        wigetPath += File.separator;
        String configPath = wigetPath + "config.xml";

        File configFile = new File(configPath);
        if (!configFile.exists()) {
            PrintUtil.info("没有找到 config.xml", projectName);
            return;
        }

        try {
            PrintUtil.info("checkBasicInfo...", projectName);
            checkBasicInfo(workPath);
            List<String> devices = getDevices();
            PrintUtil.info("get devices..." + devices.size(), projectName);
            if (devices.size() == 0) {
                PrintUtil.info("Not found Connected device", projectName);
                return;
            }
            PrintUtil.info("getAppId...", projectName);
            String appId = FileUtil.getAppId(configPath);

            if (appId == null || "".equals(appId)) {
                PrintUtil.info("Please make sure the directory is correct", projectName);
                return;
            }
            PrintUtil.info("getLoaderType...", projectName);
            getLoaderType(appId);
            for (String device : devices) {
                PrintUtil.info("pushDirOrFile... device = " + device, projectName);
                boolean isOk = pushDirOrFileCmd(device, wigetPath, appId);
                if (!isOk) {
                    PrintUtil.info("Failed to copy the file to the mobile phone, please check the connection device", projectName);
                    return;
                }
                if (pkgName.equals("com.apicloud.apploader")) {
                    PrintUtil.info("pushStartInfo...", projectName);
                    isOk = pushStartInfo(device, appId);
                    if (!isOk) {
                        PrintUtil.info("Failed to copy the file to the mobile phone, please check the connection device", projectName);
                        return;
                    }
                }
                PrintUtil.info("getApploaderVersion...", projectName);
                String currentVersion = getApploaderVersionCmd(device);
                boolean isNeedInstall = true;
                if (currentVersion != null) {
                   /* PrintUtil.info("compareAppLoaderVer...", projectName);
                    isNeedInstall = compareAppLoaderVer(currentVersion, pendingVersion);*/
                }else {
                    isOk = installAppLoaderCmd(device);
                }
                /*PrintUtil.info("isNeedInstall is " + isNeedInstall, projectName);
                if (isNeedInstall) {
                    if (currentVersion != null) {
                        PrintUtil.info("uninstallApploader...", projectName);
                        isOk = uninstallApploaderCmd(device);
                        if (!isOk) {
                            PrintUtil.info("Failed to uninstall appLoader", projectName);
                            continue;
                        }
                    }
                    PrintUtil.info("installAppLoader...", projectName);
                    isOk = installAppLoaderCmd(device);
                    if (!isOk) {
                        PrintUtil.info("Install appLoader failed", projectName);
                        continue;
                    }

                } else {
                    PrintUtil.info("stopApploader...", projectName);
                    stopApploaderCmd(device);
                }*/
                PrintUtil.info("stopApploader...", projectName);
                stopApploaderCmd(device);
                Thread.sleep(200);
                PrintUtil.info("startApploader...", projectName);
                isOk = startApploaderCmd(device);
                if (!isOk) {
                    PrintUtil.info("startApploader failed", projectName);
                    continue;
                }
                PrintUtil.info("end...device = " + device, projectName);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            PrintUtil.error("run = " + e.getMessage(), projectName);
        }

    }

    private void getWidgetPath(String filePath) {
        PrintUtil.info("------------->getWidgetPath", projectName);
        if (filePath == null || "".equals(filePath)) {
            return;
        }
        File file = new File(filePath);
        String[] fileList = file.list();
        PrintUtil.info("------------->fileList:" + JSON.toJSON(fileList), projectName);
        boolean isContinue = true;
        for (String fileS : fileList) {
            if ("config.xml".equals(fileS)) {
                String content = FileUtil.getWidgetContent(filePath + File.separator + "config.xml");
                if (isContent(fileList, content)) {
                    wigetPath = filePath;
                    isContinue = false;
                    break;
                }
            }
        }

        if (isContinue) getWidgetPath(file.getParent());
    }

    private boolean isContent(String[] listFile, String content) {
        for (String file : listFile) {
            if (file.equals(content)) {
                return true;
            }
        }
        return false;
    }

    private int checkBasicInfo(String workParh) throws IOException {
        File toolsFile = new File(workParh + "/tools");
        PrintUtil.info("tools:" + workParh + "/tools", projectName);
        PrintUtil.info("toolsFile.exists:" + toolsFile.exists(), projectName);
        PrintUtil.info("toolsFile.isDirectory:" + toolsFile.isDirectory(), projectName);
        if (!toolsFile.exists() || !toolsFile.isDirectory()) {
            return -1;
        }
        String loaderPath = workPath + "/loader" + File.separator;

        File load_conf_file = new File(loaderPath + "/load.conf");
        PrintUtil.info("load_conf_file.exists:" + load_conf_file.exists(), projectName);
        PrintUtil.info("load_conf_file:" + load_conf_file.getAbsolutePath(), projectName);
        //TODO APK更新
        File load_apk_file = new File(loaderPath + "/load.apk");
        PrintUtil.info("load_apk_file:" + load_apk_file.getAbsolutePath(), projectName);
        PrintUtil.info("load_apk_file.exists:" + load_apk_file.exists(), projectName);

        if (!load_conf_file.exists() || !load_apk_file.exists()) {
            return -1;
        }
        PrintUtil.info("isMacOS:" + isMacOS(), projectName);
        if (isMacOS()) {
            adbPath = toolsFile.getAbsolutePath() + File.separator + "adb-ios";
            String chx = "chmod +x " + adbPath;
            runCmd(chx, false);
        } else if (isLinux()) {
            adbPath = toolsFile.getAbsolutePath() + File.separator + "adb-linux";
            String chx = "chmod +x " + adbPath;
            runCmd(chx, false);
        } else {
            adbPath = toolsFile.getAbsolutePath() + File.separator + "adb.exe";
        }
        String out = (String)  runCmd("adb",false);
        if (out.contains("Android")) {
            adbPath = "adb ";
            PrintUtil.info("检测到系统有ADB环境，优先使用系统ADB", projectName);
        }
        PrintUtil.info("adbPath:" + adbPath, projectName);
        InputStreamReader read = new InputStreamReader(new FileInputStream(load_conf_file));// 考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        StringBuffer conf = new StringBuffer();
        while ((lineTxt = bufferedReader.readLine()) != null) {
            conf.append(lineTxt);
        }
        read.close();
        PrintUtil.info(conf.toString(), projectName);
        JSONObject json = JSONObject.parseObject(conf.toString());
        if (json.containsKey("version")) {
            pendingVersion = json.getString("version");
        }
        if (json.containsKey("cmdLogType")) {
            cmdLogType = json.getString("cmdLogType");
        }
        return 0;
    }

    public boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0;
    }

    public String osADB() {
        if (!isMacOS() && !isLinux()) {
            return "adb.exe";
        } else if (isLinux()) {
            return "adb-linux";
        } else {
            return "adb-ios";
        }
    }

    public boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public List<String> getDevices() throws IOException {
        List<String> devices = new ArrayList<String>();
        String cmd = adbPath + " devices ";


        Process p = (Process) runCmd(cmd, true);
        //Logger.log(p);
        BufferedReader ok_buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader err_buffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        if (ok_buffer != null) {

            String out = ok_buffer.readLine();
            while ((out = ok_buffer.readLine()) != null) {
                if (!out.contains("List of devices attached") && out.contains("device")) {
                    devices.add(out.split("\tdevice")[0]);
                    //Logger.log("out = " + out.split("\tdevice")[0]);
                }
            }
        }
        if (err_buffer != null) {
            String out = null;
            while ((out = ok_buffer.readLine()) != null) {
                //Logger.log("err out = " + out);
            }
        }
        if (p != null) {
            p.destroy();
            p = null;
        }
        return devices;
    }

    private boolean pushDirOrFileCmd(String serialNumber, String srcPath, String appId) throws IOException {


        String desPath = "/sdcard/UZMap/wgt/" + appId;
        String cachePath = System.getProperties().getProperty("user.home") + File.separator + "uztools" + File.separator + appId + File.separator + "wgt";
        if (!isMacOS() && !isLinux()) {
            FileUtil.delAllFiles(cachePath);
        } else {
            String cmd = "rm -rf " + cachePath;
            runCmd(cmd, false);
        }
        FileUtil.copyFolder(srcPath, cachePath);
        String pushCmd = adbPath + " -s " + serialNumber + " push " + cachePath + " " + desPath;
        if (!isMacOS() && !isLinux()) {
            pushCmd = "cmd.exe /C start " + pushCmd;
        }


        String out = (String) runCmd(pushCmd, false);
//		Logger.log(out);
        if (!isMacOS() && !isLinux()) {
            FileUtil.delAllFiles(cachePath);
        } else {
            String cmd = "rm -rf " + cachePath;
            runCmd(cmd, false);
        }
        if (out.contains("error: device not found")) {
            return false;
        }
        return true;
    }


    private void getLoaderType(String appId) throws IOException {
        String custom_loader_path = workPath + "/appLoader" + File.separator + "custom-loader" + File.separator + appId + File.separator;
        String custom_loader_conf = custom_loader_path + "load.conf";
        String custom_loader_ipa = custom_loader_path + "load.apk";
        File custom_loader_conf_file = new File(custom_loader_conf);
        if (custom_loader_conf_file.exists() && new File(custom_loader_ipa).exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(custom_loader_conf_file));// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            StringBuffer conf = new StringBuffer();
            while ((lineTxt = bufferedReader.readLine()) != null) {
                conf.append(lineTxt);
            }
            read.close();
            if (conf.length() == 0) {
                pkgName = "com.apicloud.apploader";
                loaderName = "apicloud-loader";
                return;
            }
            String version = "";
            JSONObject json = JSONObject.parseObject(conf.toString());
            if (json.containsKey("version")) {
                version = json.getString("version");
            }
            if (json.containsKey("packageName")) {
                pkgName = json.getString("packageName");
            }
            if (!version.equals("") && !pkgName.equals("")) {
                pendingVersion = version;
                loaderName = "custom-loader" + File.separator + appId;
            }
        } else {
            pkgName = "com.apicloud.apploader";
            loaderName = "apicloud-loader";
        }
    }


    private boolean pushStartInfo(String serialNumber, String appId) throws IOException {
        PrintUtil.info("pushStartInfo:serialNumber" + serialNumber + ",appId" + appId, projectName);
        String desPath = "/sdcard/UZMap/" + appId + "/" + "startInfo.txt";
        String srcPath = workPath + "/appLoader" + File.separator + "startInfo.txt";
        File f = new File(srcPath);
        File ff = f.getParentFile();
        ff.mkdir();
        f.createNewFile();
        PrintUtil.info("srcPath:" + srcPath, projectName);
        FileWriter fw = new FileWriter(srcPath, false);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(appId);
        pw.flush();
        fw.flush();
        pw.close();
        fw.close();
        String pushCmd = adbPath + " -s " + serialNumber + " push " + srcPath + " " + desPath;
        PrintUtil.info("pushCmd = " + pushCmd, projectName);
        String out = (String) runCmd(pushCmd, false);

        if (out.contains("error: device not found")) {
            return false;
        }
        return true;
    }

    private boolean compareAppLoaderVer(String deviceVersion, String appLoaderVersion) {
        String[] deviceVersionArray = deviceVersion.split("\\.");
        String[] appLoaderVersionArray = appLoaderVersion.split("\\.");

        for (int i = 0; i < 3; i++) {
            if (Integer.valueOf(appLoaderVersionArray[i]) > Integer.valueOf(deviceVersionArray[i])) {
                return true;
            }
        }
        return false;
    }

    private String getApploaderVersionCmd(String serialNumber) throws IOException {
        String version = null;
        String cmd = adbPath + " -s " + serialNumber + " shell dumpsys package " + pkgName;
        //Logger.log(cmd);
        String out = (String) runCmd(cmd, false);

        Pattern pattern = Pattern.compile("versionName=([0-9]{1,}.[0-9]{1,}.[0-9]{1,})");
        Matcher matcher = pattern.matcher(out);
//		Logger.log(matcher.);
        boolean result = matcher.find();

        if (result) {
            version = matcher.group(0).split("=")[1];
        }
        //Logger.log(version);
        return version;
    }

    private boolean uninstallApploaderCmd(String serialNumber) throws IOException {
        String uninstallCmd = adbPath + " -s " + serialNumber + " uninstall " + pkgName;

        String out = (String) runCmd(uninstallCmd, false);

        if (!out.contains("Success")) {
            return false;
        }
        return true;
    }

    private boolean installAppLoaderCmd(String serialNumber) throws IOException {
        String appLoader = workPath + "/loader" + File.separator + "load.apk";
        String installCmd = adbPath + " -s " + serialNumber + " install " + appLoader;

        String out = (String) runCmd(installCmd, false);
        PrintUtil.info("out  == " + out, projectName);
        if (!out.contains("Success")) {
            return false;
        }
        return true;
    }

    public void stopApploaderCmd(String serialNumber) {
        String stopCmd = adbPath + " -s " + serialNumber + " shell am force-stop " + pkgName;
        runCmd(stopCmd, false);
    }

    private boolean startApploaderCmd(String serialNumber) throws IOException {
        String appLoaderPkg = pkgName + "/com.uzmap.pkg.EntranceActivity";
        String startCmd = adbPath + " -s " + serialNumber + " shell am start -W -n " + appLoaderPkg;

        String out = (String) runCmd(startCmd, false);

        if (out.contains("error")) {
            return false;
        }

        return true;
    }


    public static Object runCmd(String cmd, boolean isReturnP) {
//		Map<String,BufferedReader> map = new HashMap<String, BufferedReader>();
//		Logger.log("cmd  == "+cmd);

        Runtime r = Runtime.getRuntime();

        Process p = null;
        StringBuffer okString = new StringBuffer();

        try {
            p = r.exec(cmd);
            if (isReturnP) {
                return p;
            }


            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader errBuf = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String out = null;

            while ((out = buf.readLine()) != null) {
//				Logger.log("out  == "+out);
                okString.append(out+"\n");

            }

            while ((out = errBuf.readLine()) != null) {
//				Logger.log(out);
                okString.append(out+"\n");
            }
            buf.close();
            errBuf.close();
            buf = null;
            errBuf = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (p != null && !isReturnP) {
                p.destroy();
                p = null;
            }


        }
        return okString.toString();
    }
}
