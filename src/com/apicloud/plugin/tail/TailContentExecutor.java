package com.apicloud.plugin.tail;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.apicloud.console.log.ConsoleLog;
import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.util.HttpClientUtil;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.layout.PlaceInGrid;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TailContentExecutor implements Disposable {
    private final Project myProject;
    private final List<Filter> myFilterList = new ArrayList<>();
    private ConsoleView consoleView = null;

    public TailContentExecutor(@NotNull Project project) {
        myProject = project;
        consoleView = createConsole(project);
        RunProperties.console(project.getName(), consoleView);
        System.out.println("consoleView++++++++++");
        WebStorm webStorm = RunProperties.getWebStorm(project.getName());
        File tempPath = new File(FileUtil.getTempDirectory().toString() + "/apicloud-intelliJ-plugin");
        if (tempPath.exists()) {
            String adbPath = tempPath.getAbsolutePath() + "/tools/" + webStorm.osADB();
            String out = (String) webStorm.runCmd("adb", false);
            if (out.contains("Android")) {
                adbPath = "adb ";
                PrintUtil.info("检测到系统有ADB环境，优先使用系统ADB", project.getName());
            }
            String finalAdbPath = adbPath;
            RunProperties.setAdbPath(finalAdbPath);
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            wifiAdb(webStorm, project, finalAdbPath);
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {

                        }
                    }
                }
            }.start();

        }
    }

    private ConsoleView createConsole(@NotNull Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        consoleBuilder.filters(myFilterList);
        ConsoleView console = consoleBuilder.getConsole();
        return console;
    }

    public void run(ToolWindow toolWindow) {
        DefaultActionGroup actions = new DefaultActionGroup();

        // Create runner UI layout
        final RunnerLayoutUi.Factory factory = RunnerLayoutUi.Factory.getInstance(myProject);
        final RunnerLayoutUi layoutUi = factory.create("Tail", "Tail", "Tail", myProject);

        final JComponent consolePanel = createConsolePanel(consoleView, actions);
        RunContentDescriptor descriptor = new RunContentDescriptor(new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
                return null;
            }

            @Override
            public String getName() {
                return "";
            }

            @Nullable
            @Override
            public Icon getIcon() {
                return new ImageIcon(TailContentExecutor.class.getResource("/com/apicloud/plugin/icons/apicloud.png"));
                //return ApicloudIcon.ApicloudIcon;
            }
        }, new DefaultExecutionResult(), layoutUi);

        final Content content = layoutUi.createContent("ConsoleContent", consolePanel, "", AllIcons.Debugger.Console, consolePanel);
        layoutUi.addContent(content, 0, PlaceInGrid.right, false);

        Disposer.register(this, descriptor);
        Disposer.register(content, consoleView);

        for (AnAction action : consoleView.createConsoleActions()) {
            actions.add(action);
        }
        ContentManager contentManager = toolWindow.getContentManager();

        contentManager.addContent(content);
        contentManager.setSelectedContent(content);
    }

    private static JComponent createConsolePanel(ConsoleView view, ActionGroup actions) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(view.getComponent(), BorderLayout.CENTER);
        panel.add(createToolbar(actions), BorderLayout.WEST);
        return panel;
    }

    private static JComponent createToolbar(ActionGroup actions) {
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actions, false);
        return actionToolbar.getComponent();
    }

    @Override
    public void dispose() {
        Disposer.dispose(this);
    }

    private void adbTools(WebStorm webStorm, Project project, String adbPath, String device) {
        String remote = "";
        String remotes[] = null;
        Object o = null;
        try {
            o = webStorm.runCmd(adbPath + " -s " + device + " shell cat /proc/net/unix |grep -a devtools_remote", false);
        } catch (Exception e) {

        }
        if (null != o) {
            remote = o.toString();
            remotes = remote.split("0000000000000000");
            remote = remotes[remotes.length - 1].substring(remotes[remotes.length - 1].indexOf("@") + 1);
        }
        if (null != remote || !"".equals(remote)) {
            try {
                o = webStorm.runCmd(adbPath + " -s " + device + "  forward tcp:9888 localabstract:" + remote, false);
                String s = HttpClientUtil.sendGet("http://localhost:9888/json", "v=1");
                if (null != s && !"".equals(s)) {
                    JSONArray array = (JSONArray) JSON.parse(s);
                    PrintUtil.info("USB同步完成，您可以将以下地址粘贴到谷歌浏览器进行手机调试", project.getName());
                    String urls = "";
                    if (null != array) {
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject jo = (JSONObject) array.get(i);
                            RunProperties.adbWifi(device + jo.getString("id"), "1");
                            PrintUtil.info("\n名称：" + new String(jo.getString("title").getBytes("gbk"), "utf-8"), project.getName());
                            PrintUtil.printUrl("url：", project.getName(), "chrome-devtools://devtools/bundled/inspector.html?ws=localhost:9888/devtools/page/" + jo.getString("id"));
                            PrintUtil.printInfoNoDate("说明： " + jo.getString("url"), project.getName());

                        }
                        PrintUtil.info(urls + "\n", project.getName());
                        Thread runnable = new Thread() {
                            public void run() {
                                ConsoleLog consoleLog = RunProperties.getConsoleLog(project.getName());
                                consoleLog.main(adbPath + "/", device);
                            }
                        };
                        runnable.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void wifiAdb(WebStorm webStorm, final Project project, String adbPath) {
        Object adbwwifi = null;
        try {
            String adbList[] = new String[0];
            try {
                adbwwifi = webStorm.runCmd(adbPath + " devices", false);
                System.out.println(adbwwifi);
                adbList = adbwwifi.toString().split("\n");
            } catch (Exception e) {

            }
            if (null != adbList && adbList.length > 0) {
                for (int i = 1; i < adbList.length; i++) {
                    final String adbDevices = adbList[i];
                    if (null != adbDevices && !"".equals(adbDevices)) {
                        String[] adb = adbDevices.split("\t");
                        String name = adb[0];
                        String status = adb[1];
                        if (name.split("\\.").length > 2) {// ip
                            RunProperties.adbIp(name, status);
                            String s = null;
                            try {
                                s = HttpClientUtil.sendGet("http://localhost:9888/json", "v=1");
                            } catch (Exception e) {

                            }
                            if (null == s || "".equals(s)) {
                                adbTools(webStorm, project, adbPath, name);
                            } else {
                                JSONArray array = (JSONArray) JSON.parse(s);
                                String urls = "";
                                if (null != array) {
                                    boolean isnew = false;
                                    for (int y = 0; y < array.size(); y++) {
                                        JSONObject jo = (JSONObject) array.get(y);
                                        if (null == RunProperties.adbWifi(name + jo.getString("id"))) {
                                            if (!isnew) {
                                                PrintUtil.info("\n调试地址更新：" + urls + "\n", project.getName());
                                                isnew = true;
                                            }
                                            RunProperties.adbWifi(name + jo.getString("id"), "1");
                                            PrintUtil.info("\n名称：" + new String(jo.getString("title").getBytes("gbk"), "utf-8"), project.getName());
                                            PrintUtil.printUrl("url：", project.getName(), "chrome-devtools://devtools/bundled/inspector.html?ws=localhost:9888/devtools/page/" + jo.getString("id"));
                                            PrintUtil.printInfoNoDate("说明： " + jo.getString("url"), project.getName());
                                        }
                                    }
                                }
                            }
                        } else {//编号
                            RunProperties.adbDevices(name, status);
                            if ("device".equals(status) || (null != RunProperties.adbDevices(name) && !"device".equals(RunProperties.adbDevices(name)))) {
                                try {
                                    webStorm.runCmd(adbPath + " tcpip 8888", false);
                                    ArrayList<String> ips = RunProperties.getIP();
                                    if (ips.size() > 0) {
                                        for (int p = 0; p < ips.size(); p++) {
                                            try {
                                                String ipstatus = RunProperties.adbIp(ips.get(p));
                                                if (null == ipstatus || (null != ipstatus && !"device".equals(ipstatus))) {
                                                    webStorm.runCmd(adbPath + " connect " + ips.get(p) + ":8888", false);
                                                    RunProperties.adbIp(ips.get(p), "1");
                                                    PrintUtil.error("检测到有已链接的USB设备，开启可用 WIFI ADB 无线调试，您现在可以断开USB数据链接，打开谷歌浏览器进行方便的无线调试", project.getName());
                                                    adbTools(webStorm, project, adbPath, name);
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

    }

}
