package com.apicloud.plugin.run;

import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.apicloud.wifisyncmanager.WifiSyncManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
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
public class PushRunAction extends AnAction implements DumbAware {

    public PushRunAction() {
        super("wifiApicloudRun Run", "wifiApicloudRun Run", new ImageIcon(PushRunAction.class.getResource("/com/apicloud/plugin/icons/PhoneWif.png")));
        System.out.println("初始化WiFi发布开关");
    }


    @Override
    public void actionPerformed(final AnActionEvent event) {
        Project project = event.getProject();
        Module module = event.getData(LangDataKeys.MODULE);
        if (RunProperties.wifiRun(project.getName())) {
            try {
                if (null == RunProperties.pushType(project.getName())) {
                    Object[] possibleValues = {"全量同步", "增量同步"};
                    String pushType = (String) JOptionPane.showInputDialog(null, "选择后将记住这个方式，直到你重新启动Wifi服务！", "请选择同步方式", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
                    RunProperties.pushType(project.getName(), pushType);
                }
                Thread ts = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                            WifiSyncManager wifiSyncManager = RunProperties.getWifiSyncManager(project.getName());
                            wifiSyncManager.main(new String[]{"1"});
                            String projectPath = event.getProject().getBaseDir().getParent().getPath();
                            String modulePath = event.getProject().getBasePath();
                            if (null != module) {
                                String moduleName = module.getName();
                                modulePath = projectPath + "/" + moduleName;
                            }
                            Thread.sleep(300);
                            if ("全量同步".equals(RunProperties.pushType(project.getName()))) {
                                wifiSyncManager.main(new String[]{"2", modulePath, projectPath});
                            } else {
                                wifiSyncManager.main(new String[]{"3", modulePath, projectPath});
                            }
                            Thread.sleep(300);
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.error(e.getMessage(), project.getName());
                        }
                    }
                };
                ts.start();
            } catch (Exception e) {
                e.printStackTrace();
                PrintUtil.error(e.getMessage(), project.getName());
            }
        } else {
            PrintUtil.error("WIFI 服务未开启", project.getName());
        }
    }
}

