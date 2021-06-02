package com.apicloud.plugin.Project;

import com.apicloud.plugin.ui.createApp.CreateAppFrom;
import com.apicloud.plugin.util.ProjectData;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.platform.WebProjectGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: lizhichao
 * Date: 2018/1/7 0007
 * Time: 21:45:19
 * Description:
 */
public class ApicloudProjectPeer implements WebProjectGenerator.GeneratorPeer<ProjectData> {

    CreateAppFrom createAppFrom = CreateAppFrom.getInstance();
    @NotNull
    @Override
    public ProjectData getSettings() {
        return createAppFrom.getProjectData();
    }

    @Override
    public void buildUI(@NotNull final SettingsStep step) {
        System.out.println("创建工程、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、");
        step.addSettingsComponent(createAppFrom.panel1);
        System.out.println("创建UI完成");

    }


    @Override
    public void addSettingsStateListener(@NotNull final WebProjectGenerator.SettingsStateListener listener) {
        System.out.println();
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        return null;
    }

    @Override
    public boolean isBackgroundJobRunning() {
        return true;
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return null;
    }

}
