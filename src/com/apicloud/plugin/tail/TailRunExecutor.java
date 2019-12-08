package com.apicloud.plugin.tail;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Window Config
 *
 * @author ob
 */
public class TailRunExecutor implements ToolWindowFactory, DumbAware {

    public static final Icon ToolWindowRun = new ImageIcon(TailRunExecutor.class.getResource("/com/apicloud/plugin/icons/apicloud.png"));
    //public static final Icon ToolWindowRun = ApicloudIcon.ApicloudIcon;
    public static String TOOLWINDOWS_ID = "Apicloud";
    private Project project = null;

    public TailRunExecutor() {
        System.out.println("TailRunExecutor ....................");
    }

    @Override
    public void init(ToolWindow window) {
        System.out.println("com.apicloud.plugin.tail.TailRunExecutor.init");
        final TailContentExecutor executor = new TailContentExecutor(project);
        Disposer.register(project, executor);
        executor.run(window);
        System.out.println("apicloud createToolWindowContent");
        window.setIcon(ToolWindowRun);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

    }


    @Override
    public boolean shouldBeAvailable(@NotNull Project project) {
        this.project = project;
        System.out.println("com.apicloud.plugin.tail.TailRunExecutor.shouldBeAvailable");
        return true;
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }


}
