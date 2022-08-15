package org.etd.generate.code.plugin.ui.extend.component;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.jgoodies.common.collect.ArrayListModel;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.utils.NotificationMessageUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class EditorPanelComponent {

    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private Editor editor;


    private JList<String> templateNames = new JBList<>();

    private String currentTemplateNameKey;

    private int currentIndex = 0;


    public EditorPanelComponent() {

        init();
        addListener();
    }

    private void init() {
        templateNames.setSelectionBackground(Color.GRAY);

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(createAddAction());
        actionGroup.add(removeAddAction());
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("add Template Toolbar", actionGroup, true);


        Splitter splitter = new Splitter(true, 0.2F);
        splitter.setFirstComponent(actionToolbar.getComponent());
        splitter.setSecondComponent(templateNames);

        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setAnchor(GridConstraints.ANCHOR_NORTHWEST);
        leftPanel.add(splitter, gridConstraints);

        initEditor();
        addActionListener();
    }

    private void initEditor() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        this.editor = editorFactory.createEditor(editorFactory.createDocument(""));
        editor.getComponent().setPreferredSize(new Dimension(580, 500));
        editor.getComponent().setMinimumSize(new Dimension(580, 500));
        editor.getComponent().setMaximumSize(new Dimension(580, 500));

        EditorSettings editorSettings = editor.getSettings();
        // 关闭虚拟空间
        editorSettings.setVirtualSpace(false);
        // 关闭标记位置（断点位置）
        editorSettings.setLineMarkerAreaShown(false);
        // 关闭缩减指南
        editorSettings.setIndentGuidesShown(false);
        // 显示行号
        editorSettings.setLineNumbersShown(true);
        // 支持代码折叠
        editorSettings.setFoldingOutlineShown(true);
        // 附加行，附加列（提高视野）
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        // 不显示换行符号
        editorSettings.setCaretRowShown(false);

        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setFill(GridConstraints.FILL_BOTH);
        gridConstraints.setAnchor(GridConstraints.ANCHOR_NORTHWEST);

        rightPanel.add(this.editor.getComponent(), gridConstraints);
    }


    private AnAction createAddAction() {
        return new AnAction(AllIcons.General.Add) {


            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
                if (BaseConstants.SYS_DEFAULT_CODE.equals(currentTemplateNameKey)) {
                    Project defaultProject = ProjectManager.getInstance().getDefaultProject();
                    NotificationMessageUtils.notifyWarning(defaultProject, "System default configuration does not allow operation");
                    return;
                }

                String value = Messages.showInputDialog("", "addTemplate", Messages.getQuestionIcon(), "", new InputValidator() {
                    @Override
                    public boolean checkInput(@NlsSafe String inputString) {
                        List<Template> templateList = templates.get(currentTemplateNameKey);
                        for (Template template : templateList) {
                            if (template.getCode().equals(inputString)) {
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    public boolean canClose(@NlsSafe String inputString) {
                        return checkInput(inputString);
                    }
                });
                if (StringUtils.isNotEmpty(value)) {
                    List<Template> templateList = templates.get(currentTemplateNameKey);
                    templateList.add(new Template(value, ""));
                    templates.put(currentTemplateNameKey, templateList);
                    refresh(currentTemplateNameKey);
                    templateNames.setSelectedValue(value,true);
                }
            }
        };
    }

    private AnAction removeAddAction() {

        return new AnAction(AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
                if (BaseConstants.SYS_DEFAULT_CODE.equals(currentTemplateNameKey)) {
                    Project defaultProject = ProjectManager.getInstance().getDefaultProject();
                    NotificationMessageUtils.notifyWarning(defaultProject, "System default configuration does not allow operation");
                    return;
                }
                int nextIndex = currentIndex > 0 ? currentIndex -1 : 0;
                templates.get(currentTemplateNameKey).remove(currentIndex);
                ArrayListModel<String> model = (ArrayListModel<String>) templateNames.getModel();
                model.remove(currentIndex);
                templateNames.setSelectedIndex(nextIndex);
            }
        };
    }


    public void refresh(String templateNameKey) {
        this.currentTemplateNameKey = templateNameKey;

        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        List<Template> templateList = templates.get(currentTemplateNameKey);
        List<String> list = templateList.stream().map(Template::getName).collect(Collectors.toList());
        ListModel<String> listModel = new ArrayListModel(list);
        if (ObjectUtils.isNotEmpty(templateNames.getModel())) {
            templateNames.removeAll();
        }
        templateNames.setModel(listModel);
        templateNames.setSelectedIndex(0);

        refreshEditor(templateNames.getSelectedValue());

        leftPanel.updateUI();
        rightPanel.updateUI();
    }

    public void addActionListener() {
        templateNames.addListSelectionListener((listener) -> {
            JBList<String> source = (JBList) listener.getSource();
            String templateName = source.getSelectedValue();
            currentIndex = source.getSelectedIndex();
            refreshEditor(templateName);
        });
    }

    public void refreshEditor(String templateName) {
        if (StringUtils.isEmpty(templateName)) {
            return;
        }
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        List<Template> templateList = templates.get(currentTemplateNameKey);
        for (int i = 0; i < templateList.size(); i++) {
            if (!templateList.get(i).getName().equals(templateName)) {
                continue;
            }
            currentIndex = i;
            writeEditor(editor, templateList.get(i).getCode(), templateName);
        }
        EditorImpl editorImpl = (EditorImpl) editor;
        editorImpl.setViewer(true);
        if(!BaseConstants.SYS_DEFAULT_CODE.equals(currentTemplateNameKey)){
            editorImpl.setViewer(false);
        }

    }

    public void writeEditor(Editor editor, String text, String templateName) {
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        WriteCommandAction.runWriteCommandAction(defaultProject, () -> editor.getDocument().setText(text));
        ((EditorEx) editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(defaultProject, templateName));
    }

    private void addListener() {
        addEditorListener();
    }

    private void addEditorListener() {
        editor.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                DocumentListener.super.documentChanged(event);
                Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
                List<Template> templateList = templates.get(currentTemplateNameKey);
                Template template1 = templateList.get(currentIndex);
                template1.setCode(event.getDocument().getText());
            }
        });
    }

}
