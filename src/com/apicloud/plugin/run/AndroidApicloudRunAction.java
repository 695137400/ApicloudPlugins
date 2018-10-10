package com.apicloud.plugin.run;

import com.apicloud.console.log.ConsoleLog;
import com.apicloud.plugin.Project.ApicloudProjectTemplateGenerator;
import com.apicloud.plugin.tail.TailRunExecutor;
import com.apicloud.plugin.util.PrintUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.io.FileUtil;

import javax.swing.*;
import java.io.File;
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
        //super("androidApicloud Run", "点击运行安卓USB同步", ApicloudIcon.AndroidApicloudIcon);
        super("androidApicloud Run", "点击运行安卓USB同步", new ImageIcon(AndroidApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/androidaoicloud.png")));
    }

    @Override
    public void actionPerformed(final AnActionEvent event) {
        try {
            PrintUtil.init(event.getProject(), TailRunExecutor.EXECUTOR_ID);
            PrintUtil.error("开始使用控制台");
            FileDocumentManager.getInstance().saveAllDocuments();
            PrintUtil.info("----------->点击安卓运行按钮");
            Properties properties = System.getProperties();
            String systemPath = properties.getProperty("idea.plugins.path");
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        File tempPath = new File(FileUtil.getTempDirectory().toString() + "/apicloud-intelliJ-plugin");
                        if (!tempPath.exists()) {
                            tempPath = FileUtil.createTempDirectory("apicloud-intelliJ-plugin", null, true);
                            ApicloudProjectTemplateGenerator.unZip(systemPath + "/ApicloudPlugins/lib/resources.jar", tempPath.getAbsolutePath() + "/", false);
                        }
                        String adbPath = tempPath.getAbsolutePath();
                        WebStorm.run(adbPath, event.getProject().getBasePath());
                        Thread.sleep(300);
                        ConsoleLog.main(tempPath.getAbsolutePath()+"/");
                    } catch (Exception e) {
                        e.printStackTrace();
                        PrintUtil.error(e.getMessage());
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.error(e.getMessage());
        }
    }
}
