package org.etd.generate.code.plugin.ui.extend.component;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.jgoodies.common.collect.ArrayListModel;
import lombok.Data;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
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


    public EditorPanelComponent() {

        init();
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

            }
        };
    }

    private AnAction removeAddAction() {
        return new AnAction(AllIcons.General.Remove) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {

            }
        };
    }

    public void refresh(String templateNameKey) {
        this.currentTemplateNameKey = templateNameKey;

        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        List<Template> templateList = templates.get(currentTemplateNameKey);
        List<String> list = templateList.stream().map(Template::getName).collect(Collectors.toList());
        ListModel<String> listModel = new ArrayListModel(list);
        templateNames.setModel(listModel);
        templateNames.setSelectedIndex(0);

        refreshEditor(templateNames.getSelectedValue());

        leftPanel.updateUI();
        rightPanel.updateUI();
//
//        String selectedValue = templateNames.getSelectedValue();
//
//        templateCode =
//
//                rightPanel.add(templateCode);
//
//        for (Template template : templateList) {
//            if (template.getName().equals(selectedValue)) {
//                templateCode.setText(template.getCode());
//            }
//        }
    }

    public void addActionListener() {
        templateNames.addListSelectionListener((listener) -> {
            JBList<String> source = (JBList) listener.getSource();
            String templateName = source.getSelectedValue();
            refreshEditor(templateName);
        });
    }

    public void refreshEditor(String templateName) {
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        List<Template> templateList = templates.get(currentTemplateNameKey);
        for (Template template : templateList) {
            if (!template.getName().equals(templateName)) {
                continue;
            }
            editor.getDocument().setText(template.getCode());
        }
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        ((EditorEx)editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(defaultProject, templateName));


    }

}
