package com.apicloud.plugin.run;

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
        System.out.println("初始化IOS同步按钮");
    }

    @Override
    public void actionPerformed(final AnActionEvent event) {
        Project project = event.getProject();
        Module module = event.getData(LangDataKeys.MODULE);
        try {
            PrintUtil.error("开始使用控制台", project.getName());
            FileDocumentManager.getInstance().saveAllDocuments();
            PrintUtil.info("----------->点击IOS运行按钮", project.getName());
            Properties properties = System.getProperties();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        Properties properties = System.getProperties();
                        String systemPath = properties.getProperty("idea.plugins.path");
                        IosLoader iosLoader = RunProperties.getIosLoader(project.getName());
                        String projectPath = event.getProject().getBaseDir().getParent().getPath();
                        String modulePath = event.getProject().getBasePath();
                        if (null != module) {
                            String moduleName = module.getName();
                            modulePath = projectPath + "/" + moduleName;
                        }
                        iosLoader.run(systemPath + "/ApicloudPlugins/lib/", modulePath);
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
