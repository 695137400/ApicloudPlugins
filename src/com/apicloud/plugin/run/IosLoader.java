package com.apicloud.plugin.run;

import com.apicloud.plugin.util.FileUtil;
import com.apicloud.plugin.util.PrintUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class IosLoader {

    private String OS = System.getProperty("os.name").toLowerCase();
    private String wigetPath = null;
    /*	public  void main(String[] args) {
            String webStormPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\webStorm-APICloud";
            String widgetPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\svn46";
            runMain(webStormPath, widgetPath);
        }*/
    private String projectName = null;

    public IosLoader(String name) {
        projectName = name;
    }

    public void run(String webStormPath, String widgetPath) {
//		String webStormPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\webStorm-APICloud";
//		String widgetPath = "C:\\Users\\gaoang\\WebstormProjects\\workspace\\svn46";
        PrintUtil.info("----------->runMain", projectName);
        getWidgetPath(widgetPath);
        if (wigetPath == null) {
            PrintUtil.info("Not found config.xml", projectName);
            return;
        }

        String appId = FileUtil.getAppId(wigetPath + File.separator + "config.xml");

        if (appId == null || "".equals(appId)) {
            PrintUtil.info("Please make sure the directory is correct", projectName);
            return;
        }

        //apicloud-loader
        String apicloud_loaderpath = webStormPath + File.separator + "appLoader" + File.separator + "apicloud-loader-ios";
        String apicloud_loader = apicloud_loaderpath + File.separator + "load.ipa";
        String apicloud_verPath = apicloud_loaderpath + File.separator + "load.conf";


        String custom_loaderpath = webStormPath + File.separator + "appLoader" + File.separator + "custom-loader-ios";
        String custom_loader = apicloud_loaderpath + File.separator + appId + File.separator + "load.ipa";
        String custom_verPath = apicloud_loaderpath + File.separator + appId + File.separator + "load.conf";

        String syncappJarPath = webStormPath + File.separator + "syncapp.jar";
//		String javaPath = null;
        String loaderpath = null;
        String loader = null;
        String verPath = null;
        if (new File(custom_loader).exists() && new File(custom_verPath).exists()) {
            loaderpath = custom_loaderpath;
            loader = custom_loader;
            verPath = custom_verPath;
        } else {
            if (new File(apicloud_loader).exists() && new File(apicloud_verPath).exists()) {
                loaderpath = apicloud_loaderpath;
                loader = apicloud_loader;
                verPath = apicloud_verPath;
            }
        }

        if (loaderpath == null || loader == null || verPath == null) {
            System.out.println("Please make sure the directory is correct");
            return;
        }

        String cachePath = System.getProperties().getProperty("user.home") + File.separator + "uztools" + File.separator + appId;
        FileUtil.copyFolder(wigetPath, cachePath);
        String runLoader = "";

        if (!isMacOS()) {
            String sysPath = System.getProperty("java.library.path");
            sysPath = sysPath.substring(0, sysPath.length() - 1);
            String jrePath = webStormPath + File.separator + "jre";
            String set_java_path = "cmd.exe /C set JAVA_HOME=" + jrePath + "&&set PATH=" + sysPath + jrePath + "\\bin;";
            String runJar = "&&" + jrePath + "\\bin\\java.exe -jar " + syncappJarPath + " " + cachePath + " " + loaderpath + " " + loader + " " + verPath;
            runLoader = set_java_path + runJar;
        } else {
            runLoader = "java -jar " + syncappJarPath + " " + cachePath + " " + loaderpath + " " + loader + " " + verPath;

        }
//		String[]  runLoader = {batPath,javaPath, "-jar",syncappJarPath , cachePath, loaderpath, loader, verPath};

        try {
            runCmd(runLoader);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!isMacOS()) {
            FileUtil.delAllFiles(cachePath);
        } else {
//			String[] cmd = {"rm -rf",cachePath};
            try {
                runCmd("rm -rf " + cachePath);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0;
    }

    public void runCmd(String cmd) throws Exception {
        // System.out.println("cmd = " + cmd[2]);
        Runtime rt = Runtime.getRuntime();
        BufferedReader br = null;
        InputStreamReader isr = null;

        Process p = rt.exec(cmd);
        isr = new InputStreamReader(p.getInputStream(), "gbk");
        br = new BufferedReader(isr);

        String msg = null;
        while ((msg = br.readLine()) != null) {
            System.out.println(msg);
        }

        if (isr != null) {
            isr.close();
        }
        if (br != null) {
            br.close();
        }
    }


    private void getWidgetPath(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            return;
        }
        File file = new File(filePath);
        String[] fileList = file.list();
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


}
