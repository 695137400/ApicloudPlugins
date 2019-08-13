package com.apicloud.plugin.ui.split.edit;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

public abstract class SplitFileEditor<E1 extends FileEditor, E2 extends FileEditor> extends UserDataHolderBase implements FileEditor {
    public static final Key<SplitFileEditor> PARENT_SPLIT_KEY = Key.create("parentSplit");

    private static final String MY_PROPORTION_KEY = "SplitFileEditor.Proportion";

    @NotNull
    protected final E1 myMainEditor;
    @NotNull
    protected final E2 mySecondEditor;
    @NotNull
    private final JComponent myComponent;

    //private SplitEditorToolbar myToolbarWrapper;

    public SplitFileEditor(@NotNull E1 mainEditor, @NotNull E2 secondEditor) {
        myMainEditor = mainEditor;
        mySecondEditor = secondEditor;

        myComponent = createComponent();

        if (myMainEditor instanceof TextEditor) {
            myMainEditor.putUserData(PARENT_SPLIT_KEY, this);
        }
        if (mySecondEditor instanceof TextEditor) {
            mySecondEditor.putUserData(PARENT_SPLIT_KEY, this);
        }
    }

    @NotNull
    private JComponent createComponent() {
        final JBSplitter splitter = new JBSplitter(false, 0.5f, 0.15f, 0.85f);
        splitter.setSplitterProportionKey(MY_PROPORTION_KEY);
        splitter.setFirstComponent(myMainEditor.getComponent());
        splitter.setSecondComponent(mySecondEditor.getComponent());

        return splitter;
    }


    private void invalidateLayout(boolean requestFocus) {
        myComponent.repaint();

        if (!requestFocus) return;

        final JComponent focusComponent = getPreferredFocusedComponent();
        if (focusComponent != null) {
            IdeFocusManager.findInstanceByComponent(focusComponent).requestFocus(focusComponent, true);
        }
    }

    @NotNull
    public E1 getMainEditor() {
        return myMainEditor;
    }

    @NotNull
    public E2 getSecondEditor() {
        return mySecondEditor;
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return myComponent;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return myMainEditor.getPreferredFocusedComponent();
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return new MyFileEditorState("XML", myMainEditor.getState(level), mySecondEditor.getState(level));
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        if (state instanceof MyFileEditorState) {
            final MyFileEditorState compositeState = (MyFileEditorState) state;
            if (compositeState.getFirstState() != null) {
                myMainEditor.setState(compositeState.getFirstState());
            }
            if (compositeState.getSecondState() != null) {
                mySecondEditor.setState(compositeState.getSecondState());
            }
            if (compositeState.getSplitLayout() != null) {
                invalidateLayout(true);
            }
        }
    }

    @Override
    public boolean isModified() {
        return myMainEditor.isModified() || mySecondEditor.isModified();
    }

    @Override
    public boolean isValid() {
        return myMainEditor.isValid() && mySecondEditor.isValid();
    }

    @Override
    public void selectNotify() {
        myMainEditor.selectNotify();
        mySecondEditor.selectNotify();
    }

    @Override
    public void deselectNotify() {
        myMainEditor.deselectNotify();
        mySecondEditor.deselectNotify();
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        myMainEditor.addPropertyChangeListener(listener);
        mySecondEditor.addPropertyChangeListener(listener);

    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        myMainEditor.removePropertyChangeListener(listener);
        mySecondEditor.removePropertyChangeListener(listener);

    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return myMainEditor.getBackgroundHighlighter();
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return myMainEditor.getCurrentLocation();
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return myMainEditor.getStructureViewBuilder();
    }

    @Override
    public void dispose() {
        Disposer.dispose(myMainEditor);
        Disposer.dispose(mySecondEditor);
    }

    static class MyFileEditorState implements FileEditorState {
        @Nullable
        private final String mySplitLayout;
        @Nullable
        private final FileEditorState myFirstState;
        @Nullable
        private final FileEditorState mySecondState;

        public MyFileEditorState(@Nullable String splitLayout, @Nullable FileEditorState firstState, @Nullable FileEditorState secondState) {
            mySplitLayout = splitLayout;
            myFirstState = firstState;
            mySecondState = secondState;
        }

        @Nullable
        public String getSplitLayout() {
            return mySplitLayout;
        }

        @Nullable
        public FileEditorState getFirstState() {
            return myFirstState;
        }

        @Nullable
        public FileEditorState getSecondState() {
            return mySecondState;
        }

        @Override
        public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level) {
            return otherState instanceof MyFileEditorState
                    && (myFirstState == null || myFirstState.canBeMergedWith(((MyFileEditorState) otherState).myFirstState, level))
                    && (mySecondState == null || mySecondState.canBeMergedWith(((MyFileEditorState) otherState).mySecondState, level));
        }
    }


}
