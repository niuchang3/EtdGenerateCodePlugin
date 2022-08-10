package org.etd.generate.code.plugin.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.GridConstraints;
import org.jetbrains.annotations.Nullable;

public abstract class BaseDialogWrapper extends DialogWrapper {


    public BaseDialogWrapper(@Nullable AnActionEvent event, boolean canBeParent, String title, int width, int height) {
        super(event.getProject(), canBeParent);
        setTitle(title);
        setSize(width, height);
    }


    protected GridConstraints getGridConstraints() {
        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
        gridConstraints.setAnchor(GridConstraints.ANCHOR_NORTH);
        return gridConstraints;
    }



}
