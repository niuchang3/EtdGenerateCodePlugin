package org.etd.generate.code.plugin.action;

import com.intellij.database.model.BaseModel;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.action.extend.GenerateCodeAction;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * IDEA DataBase 菜单中添加Etd按钮组件
 */
public class EtdGenerateCodeActionGroup extends ActionGroup {

    /**
     * 所有的Action
     */
    private static List<Action> actionList;

    /**
     * Action管理
     */
    private ActionManager actionManager = ActionManager.getInstance();

    static {
        actionList = Lists.newArrayList();
        actionList.add(new GenerateCodeAction("GenerateCode"));
    }

    /**
     * 在DataBase里面生成菜单
     *
     * @param event
     * @return
     */
    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent event) {
        if (!isGenerateMenu(event)) {
            return getEmptyAnAction();
        }
        List<AnAction> actions = Lists.newArrayList();
        for (Action anAction : actionList) {
            AnAction action = (AnAction) anAction;
            if (!isExistAction(actionManager, anAction.actionCode())) {
                actionManager.registerAction(anAction.actionCode(), action);
            }
            actions.add(action);

        }
        return actions.toArray(new AnAction[]{});
    }


    /**
     * 返回空菜单组
     *
     * @return
     */
    private AnAction[] getEmptyAnAction() {
        return AnAction.EMPTY_ARRAY;
    }


    private boolean isGenerateMenu(AnActionEvent event) {

        Object[] objects = event.getDataContext().getData(BaseConstants.DATABASE_NODES_KEY);
        Object node = objects[0];
        return ObjectUtils.isNotEmpty(getEventProject(event)) && node instanceof BaseModel.BaseNamingFamily;

    }

    private boolean isExistAction(ActionManager actionManager, String actionCode) {
        AnAction action = actionManager.getAction(actionCode);
        return ObjectUtils.isNotEmpty(action);
    }

}
