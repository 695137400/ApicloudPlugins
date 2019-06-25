package com.apicloud.plugin.run;

import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.apicloud.wifisyncmanager.WifiSyncManager;
import com.apicloud.wifisyncserver.WifiSyncServer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: lizhichao
 * Date: 2018/1/10 0010
 * Time: 10:49:44
 * Description:
 */
public class WifiApicloudRunAction extends AnAction implements DumbAware {

    public WifiApicloudRunAction() {
        super("wifiApicloudRun Run", "点击开启WIFI服务", new ImageIcon(WifiApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/wifiStop.png")));
        System.out.println("初始化WiFi运行开关");
    }


    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        System.out.println(project.getBasePath());
        try {
            Thread ts = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        PrintUtil.error("启动wifi服务----------------", project.getName());
                        RunProperties.pushType(project.getName(), null);
                        WifiSyncServer wifiSyncServer = RunProperties.getWifiSyncServer(project.getName());
                        wifiSyncServer.run(new String[]{event.getProject().getBasePath(), "1"});
                        PrintUtil.error("wifi服务启动成功", project.getName());
                        Thread.sleep(300);
                        WifiSyncManager wifiSyncManager = RunProperties.getWifiSyncManager(project.getName());
                        wifiSyncManager.main(new String[]{"1"});
                    } catch (Exception e) {
                        PrintUtil.error(e.getMessage(), project.getName());
                    }
                }
            };
            if (RunProperties.wifiRun(project.getName())) {
                event.getPresentation().setText("点击启动wifi服务");
                event.getPresentation().setIcon(new ImageIcon(WifiApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/wifiStop.png")));
                //event.getPresentation().setIcon(ApicloudIcon.wifiStopIcon);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            PrintUtil.error("wifi服务停止中，请稍等......", project.getName());
                            WifiSyncServer wifiSyncServer = RunProperties.getWifiSyncServer(project.getName());
                            wifiSyncServer.run(new String[]{event.getProject().getBasePath(), "0"});
                            Thread.sleep(1300);
                            PrintUtil.error("wifi服务已停止", project.getName());
                            RunProperties.pushType(project.getName(), null);
                        } catch (Exception e) {
                            PrintUtil.error(e.getMessage(), project.getName());
                        }
                    }
                }.start();
                ts.stop();
                RunProperties.wifiRun(project.getName(), false);
            } else {
                event.getPresentation().setText("点击停止wifi服务");
                event.getPresentation().setIcon(new ImageIcon(WifiApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/wifiApicloud.png")));
                ts.start();
                RunProperties.wifiRun(project.getName(), true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.error(e.getMessage(), project.getName());
        }
    }

}
