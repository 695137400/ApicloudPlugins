package com.apicloud.plugin.Project;

import com.apicloud.plugin.ui.createApp.CreateAppFrom;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.platform.WebProjectGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: lizhichao
 * Date: 2018/1/7 0007
 * Time: 21:45:19
 * Description:
 */
public class ApicloudProjectPeer implements WebProjectGenerator.GeneratorPeer<String> {

    private JComboBox myExecutablePathField = new JComboBox();

    @NotNull
    @Override
    public String getSettings() {
        return myExecutablePathField.getSelectedItem().toString();
    }

    @Override
    public void buildUI(@NotNull final SettingsStep step) {
        System.out.println("创建工程、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、");
        myExecutablePathField.addItem("default");
        myExecutablePathField.addItem("bottom");
        myExecutablePathField.addItem("home");
        myExecutablePathField.addItem("slide");
        myExecutablePathField.setVisible(false);
        step.addSettingsField("", myExecutablePathField);
        CreateAppFrom createAppFrom = new CreateAppFrom();
        createAppFrom.myExecutablePathField = myExecutablePathField;
        step.addSettingsField("选择模版：", createAppFrom.panel1);
        System.out.println("创建UI完成");
    }


    @Override
    public void addSettingsStateListener(@NotNull final WebProjectGenerator.SettingsStateListener listener) {
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
