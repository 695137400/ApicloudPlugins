package com.apicloud.plugin.ui.split.preview;

import com.apicloud.plugin.ui.split.edit.SplitTextEditorProvider;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import org.jetbrains.annotations.NotNull;

public class ApicloudSplitEditorProvider extends SplitTextEditorProvider {
    public ApicloudSplitEditorProvider() {
        super(new PsiAwareTextEditorProvider(), new ApicloudPreviewFileEditorProvider());
    }

    @Override
    protected FileEditor createSplitEditor(@NotNull final FileEditor firstEditor, @NotNull FileEditor secondEditor) {
        if (!(firstEditor instanceof TextEditor) || !(secondEditor instanceof ApicloudPreviewFileEditor)) {
            throw new IllegalArgumentException("Main editor should be TextEditor");
        }
        return new ApicloudSplitEditor(((TextEditor) firstEditor), ((ApicloudPreviewFileEditor) secondEditor));
    }
}
