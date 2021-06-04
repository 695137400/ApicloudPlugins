package com.apicloud.plugin.tail;

import com.apicloud.plugin.run.WebStorm;
import com.apicloud.plugin.util.PrintUtil;
import com.apicloud.plugin.util.RunProperties;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
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
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class TailContentExecutor implements Disposable {
    private final Project myProject;
    private ConsoleView consoleView = null;

    public TailContentExecutor(@NotNull Project project) {
        myProject = project;
        try {
            consoleView = createConsole(project);
            RunProperties.console(project.getName(), consoleView);
            System.out.println("consoleView++++++++++");
            if (null != PrintUtil.msg && !"".equalsIgnoreCase(PrintUtil.msg)) {
                PrintUtil.info(PrintUtil.msg, project.getName());
            }
            String path = RunProperties.getAdbPath();
            if (null == path || "".equalsIgnoreCase(path)) {
                Properties properties = System.getProperties();
                String systemPath = properties.getProperty("idea.plugins.path");
                WebStorm storm = RunProperties.getWebStorm(project.getName());
                if (storm.isMacOS() || storm.isLinux()) {
                    String chx = "chmod +x " + systemPath + "/ApicloudPlugins/lib/tools/adb-ios";
                    storm.runCmd(chx, false);
                    chx = "chmod +x " + systemPath + "/ApicloudPlugins/lib/tools/adb-linux";
                    storm.runCmd(chx, false);
                }
                if (storm.isMacOS()) {
                    RunProperties.setAdbPath(systemPath + "/ApicloudPlugins/lib/tools/adb-ios");
                } else if (storm.isLinux()) {
                    RunProperties.setAdbPath(systemPath + "/ApicloudPlugins/lib/tools/adb-linux");
                } else {
                    RunProperties.setAdbPath(systemPath + "/ApicloudPlugins/lib/tools/adb.exe");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ConsoleView createConsole(@NotNull Project project) {

        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
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

}
