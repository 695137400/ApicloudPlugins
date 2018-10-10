package com.apicloud.plugin.run;

import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.wifisyncmanager.WifiSyncManager;
import com.apicloud.wifisyncserver.WifiSyncServer;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;

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
        //super("wifiApicloudRun Run", "点击开启WIFI服务", ApicloudIcon.wifiStopIcon);
    }

    private boolean isRun = false;

    @Override
    public void actionPerformed(AnActionEvent event) {

        try {
            PrintUtil.init(event.getProject(), "Run");
            Thread ts = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(300);
                        PrintUtil.error("启动wifi服务----------------");
                        PushRunAction.pushType = "";
                        WifiSyncServer.run(new String[]{event.getProject().getBasePath(), "1"});
                        PrintUtil.error("wifi服务启动成功");
                        Thread.sleep(300);
                        WifiSyncManager.main(new String[]{"1"});
                        PushRunAction.wifiOpen = true;
                    } catch (Exception e) {
                        PrintUtil.error(e.getMessage());
                    }
                }
            };
            if (isRun) {
                PushRunAction.wifiOpen = false;
                event.getPresentation().setText("点击启动wifi服务");
                event.getPresentation().setIcon(new ImageIcon(WifiApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/wifiStop.png")));
                //event.getPresentation().setIcon(ApicloudIcon.wifiStopIcon);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            PrintUtil.error("wifi服务停止中，请稍等......");
                            WifiSyncServer.run(new String[]{event.getProject().getBasePath(), "0"});
                            Thread.sleep(1300);
                            PrintUtil.error("wifi服务已停止");
                            PushRunAction.pushType = "";
                        } catch (Exception e) {
                            PrintUtil.error(e.getMessage());
                        }
                    }
                }.start();
                ts.stop();
                isRun = false;
            } else {
                event.getPresentation().setText("点击停止wifi服务");
                event.getPresentation().setIcon(new ImageIcon(WifiApicloudRunAction.class.getResource("/com/apicloud/plugin/icons/wifiApicloud.png")));
                //event.getPresentation().setIcon(ApicloudIcon.wifiApicloudIcon);
                ts.start();
                isRun = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.error(e.getMessage());
        }
    }

}
