package com.apicloud.plugin.run;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apicloud.console.log.ConsoleLog;
import com.apicloud.plugin.util.HttpClientUtil;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: lizhichao
 * Date: 2018/1/10 0010
 * Time: 10:49:44
 * Description:
 */
public class AndroidApicloudRunAction extends AnAction implements DumbAware {

    public AndroidApicloudRunAction() {
        super("androidApicloud Run", "点击运行安卓USB同步", new ImageIcon(AndroidApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/androidaoicloud.png")));
        System.out.println("初始化安卓运行按钮");
    }

    @Override
    public void actionPerformed(final AnActionEvent event) {
        Project project = event.getProject();
        Module module = event.getData(LangDataKeys.MODULE);
        try {
            PrintUtil.error("开始使用控制台", project.getName());
            FileDocumentManager.getInstance().saveAllDocuments();
            PrintUtil.info("----------->点击安卓运行按钮", project.getName());
            Properties properties = System.getProperties();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        WebStorm webStorm = RunProperties.getWebStorm(project.getName());
                        String adbPath = RunProperties.getAdbPath();
                        Properties properties = System.getProperties();
                        String systemPath = properties.getProperty("idea.plugins.path");
                        String projectPath = event.getProject().getBaseDir().getParent().getPath();
                        String modulePath = event.getProject().getBasePath();
                        if (null != module) {
                            String moduleName = module.getName();
                            modulePath = projectPath + "/" + moduleName;
                        }
                        webStorm.run(systemPath + "/ApicloudPlugins/lib/", modulePath);
                        RunProperties.wifiAdb(webStorm,project.getName(),adbPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                        PrintUtil.error(e.getMessage(), project.getName());
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.error(e.getMessage(), project.getName());
        }
    }

}
