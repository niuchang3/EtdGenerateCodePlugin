package org.etd.generate.code.plugin.ui.extend;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.uiDesigner.core.GridConstraints;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.etd.generate.code.plugin.bean.Settings;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.etd.generate.code.plugin.context.ApplicationContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.ui.BaseConfigurable;
import org.etd.generate.code.plugin.ui.extend.component.EditorPanelComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class EtdGenerateCodeConfigurable extends BaseConfigurable {

    private JPanel mainPanel;
    private JTextField author;
    private JComboBox templateGroup;
    private JPanel topPanel;
    private JPanel templateGroupPanel;
    private JPanel bottomPanel;


    //    private JTextField author;
//
//    private JComboBox templateGroup;
//
//    private JPanel bottomPanel;
//    private JComboBox templateGroup;
//    private JTextField author;
//    private JPanel templateGroupButtons;
//
    private EditorPanelComponent editorPanelComponent = new EditorPanelComponent();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "EtdGenerateCode";
    }


    @Override
    protected JComponent getMainPanel() {
        initData();
        return mainPanel;
    }

    @Override
    public boolean isModified() {

        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    protected GridConstraints getGridConstraints() {
        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setFill(GridConstraints.FILL_BOTH);
        gridConstraints.setAnchor(GridConstraints.ANCHOR_NORTH);
        return gridConstraints;
    }

    private void initData() {
        author.setText(getDbAuthor());
        bottomPanel.add(editorPanelComponent.getMainPanel(), getGridConstraints());


        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(createAddAction());
        actionGroup.add(removeAddAction());
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("Template Group Buttons Toolbar", actionGroup, true);

        Splitter splitter = new Splitter(false, 0.5F);

        templateGroup = new JComboBox();
        templateGroup.setPreferredSize(new Dimension(200,-1));

        splitter.setFirstComponent(templateGroup);
        splitter.setSecondComponent(actionToolbar.getComponent());

        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setAnchor(GridConstraints.ANCHOR_NORTHWEST);
        templateGroupPanel.add(splitter, gridConstraints);
        templateGroupPanel.updateUI();


        refresh("");
    }

    private AnAction createAddAction() {
        return new AnAction(AllIcons.General.Add) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                String value = Messages.showInputDialog("", "AddTemplateGroup", Messages.getQuestionIcon(), "", new InputValidator() {
                    @Override
                    public boolean checkInput(@NlsSafe String inputString) {
                        ApplicationContext context = GenerateCodeContextHelper.getContext();
                        Settings setting = context.getSetting();
                        Map<String, List<Template>> templates = setting.getTemplates();
                        return ObjectUtils.isNotEmpty(templates) && !templates.containsKey(inputString);
                    }

                    @Override
                    public boolean canClose(@NlsSafe String inputString) {
                        return checkInput(inputString);
                    }
                });
                if (StringUtils.isNotEmpty(value)) {
                    addTemplates(value, new Template("demo.java.vm", ""));
                    refresh(value);
                }

            }
        };
    }

    private AnAction removeAddAction() {
        return new AnAction(AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                Object selectedItem = templateGroup.getSelectedItem();
                templateGroup.removeItem(selectedItem);
            }
        };
    }


    private void refresh(String selectTemplateGroup) {
        refreshTemplateGroup(selectTemplateGroup);
        editorPanelComponent.refresh((String) templateGroup.getSelectedItem());
    }


    private void refreshTemplateGroup(String selectTemplateGroup) {
        templateGroup.removeAll();
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        for (Map.Entry<String, List<Template>> stringListEntry : templates.entrySet()) {
            templateGroup.addItem(stringListEntry.getKey());
        }
        if (StringUtils.isEmpty(selectTemplateGroup)) {
            templateGroup.setSelectedIndex(0);
            return;
        }
        templateGroup.setSelectedItem(selectTemplateGroup);
    }
}
