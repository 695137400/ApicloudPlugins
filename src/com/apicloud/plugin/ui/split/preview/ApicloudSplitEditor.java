/*
package com.apicloud.plugin.ui.split.preview;

import com.apicloud.plugin.ui.split.edit.SplitFileEditor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class ApicloudSplitEditor extends SplitFileEditor<TextEditor, ApicloudPreviewFileEditor> implements TextEditor {
    public ApicloudSplitEditor(@NotNull TextEditor mainEditor, @NotNull ApicloudPreviewFileEditor secondEditor) {
        super(mainEditor, secondEditor);
    }

    @NotNull
    @Override
    public String getName() {
        return "Markdown split editor";
    }

    @NotNull
    @Override
    public Editor getEditor() {
        return getMainEditor().getEditor();
    }

    @Override
    public boolean canNavigateTo(@NotNull Navigatable navigatable) {
        return getMainEditor().canNavigateTo(navigatable);
    }

    @Override
    public void navigateTo(@NotNull Navigatable navigatable) {
        getMainEditor().navigateTo(navigatable);
    }

}
*/
