package com.apicloud.console.log;

import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.util.PrintUtil;
import com.intellij.openapi.project.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-5-13-0013<br/>
 * Time: 14:26:58<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */
public class ConsoleLog {

    private String projectName = null;

    public ConsoleLog(String name) {
        projectName = name;
    }

    public void main(String workPath, String adbDevices) {
        String adbPath;
        if (OS.isMacOS()) {
            adbPath = workPath + "tools" + File.separator + "adb-ios";
            String chx = "chmod +x " + adbPath;
            WebStorm.runCmd(chx, false);
        } else if (OS.isLinux()) {
            adbPath = workPath + "tools" + File.separator + "adb-linux";
            String chx = "chmod +x " + adbPath;
            WebStorm.runCmd(chx, false);
        } else {
            adbPath = workPath + "tools" + File.separator + "adb.exe";
        }

        String[] parameters = new String[]{adbPath, "-s", adbDevices, "logcat", "-v", "time", "-s", "app3c"};

        try {
            if (!getDevices(adbPath)) {
                PrintUtil.error("未发现链接中的手机", projectName);
                return;
            }
        } catch (IOException var5) {
            var5.printStackTrace();
            PrintUtil.error(var5.getMessage(), projectName);
            return;
        }

        if (!(new File(adbPath)).exists()) {
            PrintUtil.error("ADB does not exist", projectName);
        } else {
            runCmd(parameters, true);
        }
    }

    public Process runCmd(String[] cmd, boolean isLog) {
        Runtime rt = Runtime.getRuntime();
        BufferedReader br = null;
        InputStreamReader isr = null;

        try {
            Process p = rt.exec(cmd);
            if (!isLog) {
                Process var8 = p;
                return var8;
            }

            isr = new InputStreamReader(p.getInputStream());
            br = new BufferedReader(isr);
            String msg = null;
            PrintUtil.error("Log has started", projectName);

            while ((msg = br.readLine()) != null) {
                PrintUtil.info(msg, projectName);
            }

            PrintUtil.error("Not found Connected device", projectName);
        } catch (Exception var17) {
            var17.printStackTrace();
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }

                if (br != null) {
                    br.close();
                }
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

        return null;
    }

    private boolean getDevices(String adbPath) throws IOException {
        List<String> devices = new ArrayList();
        String[] parameters = new String[]{adbPath, "devices"};
        Process p = runCmd(parameters, false);
        BufferedReader ok_buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader err_buffer = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        String out;
        if (ok_buffer != null) {
            out = ok_buffer.readLine();

            while ((out = ok_buffer.readLine()) != null) {
                if (!out.contains("List of devices attached") && out.contains("device")) {
                    devices.add(out.split("\tdevice")[0]);
                }
            }
        }

        if (err_buffer != null) {
            out = null;

            while (ok_buffer.readLine() != null) {
                ;
            }
        }

        if (p != null) {
            p.destroy();
            p = null;
        }

        return devices.size() > 0;
    }

}
