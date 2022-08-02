package org.etd.generate.code.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsActions;
import org.etd.generate.code.plugin.context.GenerateCodeContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAction extends AnAction implements Action {

    public AbstractAction(@Nullable @NlsActions.ActionText String text) {
        super(text);
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        GenerateCodeContextHelper.clean();
        initContext(e);
        action(e);
    }

    protected abstract void action(@NotNull AnActionEvent e);


    private void initContext(@NotNull AnActionEvent e) {
        GenerateCodeContext context = new GenerateCodeContext(e);
        GenerateCodeContextHelper.setContext(context);
    }

}
