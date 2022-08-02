package org.etd.generate.code.plugin.action.extend;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsActions;
import org.etd.generate.code.plugin.action.AbstractAction;
import org.etd.generate.code.plugin.action.Action;
import org.etd.generate.code.plugin.ui.extend.GenerateCodeMainPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenerateCodeAction extends AbstractAction implements Action {

    /**
     * 菜单Code
     */
    private final String GENERATE_CODE_ACTION = "org.etd.generate.code.plugin.action.extend.GenerateCodeAction";


    public GenerateCodeAction(@Nullable @NlsActions.ActionText String text) {
        super(text);
    }


    @Override
    protected void action(@NotNull AnActionEvent event) {
        new GenerateCodeMainPage(event).show();
    }

    @Override
    public String actionCode() {
        return GENERATE_CODE_ACTION;
    }
}
