package com.apicloud.plugin.ui.split.preview;

import com.apicloud.plugin.ui.ApicloudConfigEdit;
import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.<br/>
 * User: lizhichao<br/>
 * Date: 2018-7-17-0017<br/>
 * Time: 12:57:09<br/>
 * Author:lizhichao<br/>
 * Description: <span style="color:#63D3E9"></span><br/>
 */

public class ApicloudPreviewFileEditor extends UserDataHolderBase implements FileEditor {


    @NotNull
    private final JPanel myHtmlPanelWrapper;

    @NotNull
    private final Alarm myPooledAlarm = new Alarm(Alarm.ThreadToUse.POOLED_THREAD, this);

    public ApicloudPreviewFileEditor(@NotNull VirtualFile file) {
//布局
        System.out.println("-------------------2");
        ApicloudConfigEdit cfgEdit = new ApicloudConfigEdit();
        cfgEdit.setConfigPath(file.getPath());
        cfgEdit.init();
        JScrollPane text2 = new JScrollPane(cfgEdit.getContentPane());

        JPanel j1 = new JPanel();
        GridConstraints deleteConstraints = new GridConstraints();
        deleteConstraints.setColumn(0);
        deleteConstraints.setRow(0);
        deleteConstraints.setRowSpan(1);
        deleteConstraints.setColSpan(1);
        deleteConstraints.setVSizePolicy(0);
        deleteConstraints.setHSizePolicy(3);
        deleteConstraints.setAnchor(0);
        deleteConstraints.setFill(3);
        deleteConstraints.setIndent(0);
        deleteConstraints.setUseParentLayout(false);
        GridLayoutManager pl = new GridLayoutManager(1, 1);
        pl.setSameSizeHorizontally(false);
        pl.setSameSizeVertically(false);
        pl.setHGap(-1);
        pl.setVGap(-1);
        pl.setMargin(new Insets(0, 0, 0, 0));
        j1.setLayout(pl);
        j1.add(text2, deleteConstraints);
        myHtmlPanelWrapper = j1;
    }


    @NotNull
    @Override
    public JComponent getComponent() {
        return myHtmlPanelWrapper;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return null;
    }

    @NotNull
    @Override
    public String getName() {
        return "Apicloud HTML Preview";
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        System.out.println("-------------------1");
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void selectNotify() {
        myPooledAlarm.cancelAllRequests();
    }

    @Override
    public void deselectNotify() {
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
