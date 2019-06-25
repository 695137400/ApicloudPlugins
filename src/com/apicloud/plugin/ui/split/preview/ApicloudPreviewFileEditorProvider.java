package com.apicloud.plugin.ui.split.preview;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.WeighedFileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class ApicloudPreviewFileEditorProvider extends WeighedFileEditorProvider {

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        return "config.xml".equals(file.getName());//文件名/类型
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        ApicloudPreviewFileEditor markdownPreviewFileEditor = new ApicloudPreviewFileEditor(file);
     try {
            return ProjectLocator.computeWithPreferredProject(file, project, (ThrowableComputable<FileEditor, Throwable>) () -> markdownPreviewFileEditor);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        return markdownPreviewFileEditor;
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "markdown-preview-editor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_AFTER_DEFAULT_EDITOR;
    }
}
