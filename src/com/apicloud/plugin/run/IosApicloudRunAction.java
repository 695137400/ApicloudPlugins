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
public class IosApicloudRunAction extends AnAction implements DumbAware {

    public IosApicloudRunAction() {
        super("IOSApicloud Run", "点击运行IOS同步", new ImageIcon(IosApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/iosapicloud.png")));
        //super("IOSApicloud Run", "点击运行IOS同步", ApicloudIcon.IOSApicloudIcon);
    }

    @Override
    public void actionPerformed(final AnActionEvent event) {
        try {
            PrintUtil.init(event.getProject(), TailRunExecutor.EXECUTOR_ID);
            PrintUtil.error("开始使用控制台");
            FileDocumentManager.getInstance().saveAllDocuments();
            PrintUtil.info("----------->点击IOS运行按钮");
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
                        IosLoader.run(adbPath, event.getProject().getBasePath());
                        Thread.sleep(300);
                        ConsoleLog.main(tempPath.getAbsolutePath() + "/");
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
