package com.apicloud.plugin.tail;

import com.apicloud.plugin.util.PrintUtil;
import com.intellij.execution.*;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.execution.ui.RunnerLayoutUi;
import com.intellij.execution.ui.layout.PlaceInGrid;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.content.Content;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TailContentExecutor implements Disposable {
    private final Project myProject;
    private final List<Filter> myFilterList = new ArrayList<>();
    private ConsoleView consoleView = null;

    public TailContentExecutor(@NotNull Project project) {
        myProject = project;
        consoleView = createConsole(project);
        PrintUtil.console = consoleView;
    }

    private ConsoleView createConsole(@NotNull Project project) {
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        consoleBuilder.filters(myFilterList);
        ConsoleView console = consoleBuilder.getConsole();
        return console;
    }

    public void run(String title) {
        FileDocumentManager.getInstance().saveAllDocuments();
        Executor executor = ExecutorRegistry.getInstance().getExecutorById(title);
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
        RunContentManager rum = ExecutionManager.getInstance(myProject).getContentManager();
        rum .showRunContent(executor, descriptor);
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
