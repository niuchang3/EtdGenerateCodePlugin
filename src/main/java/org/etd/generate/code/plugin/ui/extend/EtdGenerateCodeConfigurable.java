package org.etd.generate.code.plugin.ui.extend;

import com.google.gson.Gson;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.impl.LoadTextUtil;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.uiDesigner.core.GridConstraints;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.etd.generate.code.plugin.bean.Settings;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.etd.generate.code.plugin.context.ApplicationContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.ui.BaseConfigurable;
import org.etd.generate.code.plugin.ui.extend.component.EditorPanelComponent;
import org.etd.generate.code.plugin.utils.NotificationMessageUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class EtdGenerateCodeConfigurable extends BaseConfigurable {

    private JPanel mainPanel;
    private JTextField author;
    private JComboBox templateGroup;
    private JPanel topPanel;
    private JPanel templateGroupPanel;
    private JPanel bottomPanel;

    private JButton importButton;

    private JButton exportButton;

    private Settings settings;


    private EditorPanelComponent editorPanelComponent = new EditorPanelComponent();

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "EtdGenerateCode";
    }


    @Override
    protected JComponent getMainPanel() {
        initData();
        addActionListener();
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        if (!getDbAuthor().equals(author.getText())) {
            return true;
        }
        Settings dbSetting = GenerateCodeContextHelper.getContext().getSetting();
        if (ObjectUtils.isNotEmpty(settings) && !dbSetting.equals(settings)) {
            return true;
        }
        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
        ApplicationContext context = GenerateCodeContextHelper.getContext();
        return !context.getSetting().getTemplates().equals(templates);
    }

    @Override
    public void apply() throws ConfigurationException {

        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);

        ApplicationContext context = GenerateCodeContextHelper.getContext();
        Settings dbSettings = context.getSetting();



        dbSettings.setAuthor(author.getText());
        dbSettings.setCurrentTemplateGroup((String) templateGroup.getSelectedItem());

        if (settings != null) {
            dbSettings.setAuthor(settings.getAuthor());
            dbSettings.setCurrentTemplateGroup(settings.getCurrentTemplateGroup());
            dbSettings.setTypeMappings(settings.getTypeMappings());
            for (Map.Entry<String, List<Template>> entry : settings.getTemplates().entrySet()) {
                if (entry.getKey().equals(BaseConstants.SYS_DEFAULT_CODE)) {
                    continue;
                }
                if (templates.containsKey(entry.getKey())) {
                    continue;
                }
                templates.put(entry.getKey(), entry.getValue());
            }
        }
        dbSettings.setTemplates(templates);
        context.getSettingsStorage().loadState(dbSettings);
        settings = null;
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
        actionGroup.add(updateAddAction());
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("Template Group Buttons Toolbar", actionGroup, true);

        Splitter splitter = new Splitter(false, 0.5F);

        templateGroup = new JComboBox();
        templateGroup.setPreferredSize(new Dimension(200, -1));

        splitter.setFirstComponent(templateGroup);
        splitter.setSecondComponent(actionToolbar.getComponent());

        GridConstraints gridConstraints = new GridConstraints();
        gridConstraints.setAnchor(GridConstraints.ANCHOR_NORTHWEST);
        templateGroupPanel.add(splitter, gridConstraints);
        templateGroupPanel.updateUI();

        Settings setting = GenerateCodeContextHelper.getContext().getSetting();
        refresh(setting.getCurrentTemplateGroup());
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
                String selectedItem = (String) templateGroup.getSelectedItem();
                if (BaseConstants.SYS_DEFAULT_CODE.equals(selectedItem)) {
                    Project defaultProject = ProjectManager.getInstance().getDefaultProject();
                    NotificationMessageUtils.notifyWarning(defaultProject, "System default configuration does not allow operation");
                    return;
                }
                templateGroup.removeItem(selectedItem);
                removeTemplate(selectedItem);
            }
        };
    }

    private AnAction updateAddAction() {
        return new AnAction(AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                String selectedItem = (String) templateGroup.getSelectedItem();
                if (BaseConstants.SYS_DEFAULT_CODE.equals(selectedItem)) {
                    Project defaultProject = ProjectManager.getInstance().getDefaultProject();
                    NotificationMessageUtils.notifyWarning(defaultProject, "System default configuration does not allow operation");
                    return;
                }
                String value = Messages.showInputDialog("", "UpdateTemplateGroup", Messages.getQuestionIcon(), selectedItem, new InputValidator() {
                    @Override
                    public boolean checkInput(@NlsSafe String inputString) {
                        Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
                        return ObjectUtils.isNotEmpty(templates) && !templates.containsKey(inputString) && !selectedItem.equals(inputString);
                    }

                    @Override
                    public boolean canClose(@NlsSafe String inputString) {
                        return checkInput(inputString);
                    }
                });
                if (StringUtils.isNotEmpty(value)) {
                    Map<String, List<Template>> templates = (Map<String, List<Template>>) GenerateCodeContextHelper.getAttribute(BaseConstants.SYS_TEMPLATE_CODE);
                    List<Template> oldTemplateList = templates.get(selectedItem);
                    List<Template> newTemplateList = Lists.newArrayList();
                    if (CollectionUtils.isNotEmpty(oldTemplateList)) {
                        newTemplateList.addAll(oldTemplateList);
                    } else {
                        newTemplateList.add(new Template("demo.java.vm", ""));
                    }
                    removeTemplate(selectedItem);
                    for (Template template : newTemplateList) {
                        addTemplates(value, template);
                    }
                    refresh(value);
                }

            }
        };
    }


    private void refresh(String selectTemplateGroup) {
        refreshTemplateGroup(selectTemplateGroup);
        editorPanelComponent.refresh((String) templateGroup.getSelectedItem());
    }


    private void refreshTemplateGroup(String selectTemplateGroup) {
        templateGroup.removeAllItems();
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

    public void addActionListener() {
        templateGroupActionListener();
        importButtonActionListener();
        exportButtonActionListener();
    }

    private void templateGroupActionListener() {
        templateGroup.addActionListener(listener -> {
            JComboBox source = (JComboBox) listener.getSource();
            String selectedItem = (String) source.getSelectedItem();
            refresh(selectedItem);
        });
    }

    private void exportButtonActionListener() {
        exportButton.addActionListener(listener -> {
            Project defaultProject = ProjectManager.getInstance().getDefaultProject();
            FileSaverDialog saveFileDialog = FileChooserFactory.getInstance().createSaveFileDialog(new FileSaverDescriptor("Save Config As Json", "Save to"), defaultProject);
            VirtualFileWrapper saveFile = saveFileDialog.save((VirtualFile) null, "defaultConfig.json");
            FileUtil.createIfDoesntExist(saveFile.getFile());
            WriteCommandAction.runWriteCommandAction(defaultProject, () -> {
                Settings setting = GenerateCodeContextHelper.getContext().getSetting();
                String json = new Gson().toJson(setting);
                VirtualFile file = saveFile.getVirtualFile();
                try {
                    file.setBinaryContent(json.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileDocumentManager.getInstance().reloadFiles(file);
            });
        });
    }

    private void importButtonActionListener() {
        importButton.addActionListener(listener -> {
            Project defaultProject = ProjectManager.getInstance().getDefaultProject();
            VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFileDescriptor("json"), defaultProject, null);
            if (virtualFile == null) {
                Messages.showWarningDialog("config file not found！", "");
            }

            String jsonStr = LoadTextUtil.loadText(virtualFile).toString();
            settings = new Gson().fromJson(jsonStr, Settings.class);
        });
    }
}
