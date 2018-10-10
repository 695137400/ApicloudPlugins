package com.apicloud.plugin.run;

import com.apicloud.plugin.tail.TailRunExecutor;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.wifisyncmanager.WifiSyncManager;
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
public class PushRunAction extends AnAction implements DumbAware {

    public PushRunAction() {
        // super("wifiApicloudRun Run", "wifiApicloudRun Run", ApicloudIcon.PhonewifiApicloudIcon);
        super("wifiApicloudRun Run", "wifiApicloudRun Run", new ImageIcon(PushRunAction.class.getResource("/com/apicloud/plugin/icons/PhoneWif.png")));
    }

    public static boolean wifiOpen = false;
    public static String pushType = "";

    @Override
    public void actionPerformed(final AnActionEvent event) {
        if (wifiOpen) {
            try {
                if ("".equals(pushType)) {
                    Object[] possibleValues = {"全量同步", "增量同步"};
                    pushType = (String) JOptionPane.showInputDialog(null, "选择后将记住这个方式，直到你重新启动Wifi服务！", "请选择同步方式", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                }
                PrintUtil.init(event.getProject(), TailRunExecutor.EXECUTOR_ID);
                Thread ts = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                            WifiSyncManager.main(new String[]{"1"});
                            Thread.sleep(300);
                            if ("全量同步".equals(pushType)) {
                                WifiSyncManager.main(new String[]{"2", event.getProject().getBasePath(), event.getProject().getBaseDir().getParent().getPath()});
                            } else {
                                WifiSyncManager.main(new String[]{"3", event.getProject().getBasePath(), event.getProject().getBaseDir().getParent().getPath()});
                            }
                            Thread.sleep(300);
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.error(e.getMessage());
                        }
                    }
                };
                ts.start();
            } catch (Exception e) {
                e.printStackTrace();
                PrintUtil.error(e.getMessage());
            }
        } else {
            PrintUtil.error("WIFI 服务未开启");
        }
    }
}

