package org.etd.generate.code.plugin.ui.extend;

import com.intellij.database.model.DasTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.etd.generate.code.plugin.bean.TableInfo;
import org.etd.generate.code.plugin.context.GenerateCodeContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.listener.PackageButtonActionListener;
import org.etd.generate.code.plugin.listener.PathButtonActionListener;
import org.etd.generate.code.plugin.ui.BaseDialogWrapper;
import org.etd.generate.code.plugin.ui.extend.component.AdvancedPanelComponent;
import org.etd.generate.code.plugin.ui.extend.component.CommonPanelComponent;
import org.etd.generate.code.plugin.utils.NotificationMessageUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Slf4j
public class GenerateCodeMainPage extends BaseDialogWrapper {

    private JPanel centerPanel;

    private JTextField pathField;

    private JButton pathChooseButton;

    private JTextField packageField;

    private JButton packageButton;

    private JComboBox tablesField;

    private ButtonGroup generateTypeGroup = new ButtonGroup();

    private JRadioButton commonButton;
    private JRadioButton advancedButton;

    private JPanel commonPanel;
    private JPanel advancedPanel;

    private JButton joinButton;
    private JButton deleteJoinButton;

    private CommonPanelComponent commonPanelComponent = new CommonPanelComponent();

    private List<AdvancedPanelComponent> advancedPanelComponents = Lists.newArrayList();


    public GenerateCodeMainPage(AnActionEvent event) {
        super(event, false, "EtdGenerateCode", 500, 300);
        initDialog();
    }


    protected void initDialog() {
        commonPanel.add(commonPanelComponent.getMainPanel(), getGridConstraints());
        initTablesField();
        // 填充默认的代码生成路径
        pathField.setText(GenerateCodeContextHelper.getContext().getProject().getBasePath());
        pathField.setEditable(false);
//        packageField.setEditable(false);
        showButton(false);
        generateTypeGroup.add(commonButton);
        generateTypeGroup.add(advancedButton);
        addActionListener();

        init();
    }


    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        return centerPanel;
    }

    private void addActionListener() {
        addActionListenerPathButton();
        addActionListenerPackageButton();
        addActionListenerCommonButton();
        addActionListenerAdvancedButton();
        addActionListenerJoinButton();
        addActionListenerDeleteButton();
    }

    private void addActionListenerPathButton() {
        pathChooseButton.addActionListener(new PathButtonActionListener(s -> {
            pathField.setText((String) s);
        }));
    }

    private void addActionListenerPackageButton() {
        packageButton.addActionListener(new PackageButtonActionListener(s -> {
            packageField.setText((String) s);
        }));
    }

    private void initTablesField() {
        List<DasTable> tables = GenerateCodeContextHelper.getContext().getTables();
        for (DasTable table : tables) {
            tablesField.addItem(table.getName());
        }
    }

    private void showButton(boolean flag) {
        deleteJoinButton.setVisible(flag);
        joinButton.setVisible(flag);
        advancedPanel.setVisible(flag);
    }

    private void addActionListenerCommonButton() {
        commonButton.addActionListener(e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (!source.isSelected()) {
                return;
            }
            showButton(false);
        });
    }

    private void addActionListenerAdvancedButton() {
        advancedButton.addActionListener(e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (!source.isSelected()) {
                return;
            }
            showButton(true);
        });
    }

    private void addActionListenerJoinButton() {

        joinButton.addActionListener(tablesField -> {
            GenerateCodeContext context = GenerateCodeContextHelper.getContext();
            if (advancedPanelComponents.size() >= 3) {
                NotificationMessageUtils.notifyWarning(context.getProject(), "Up to three items can be associated");
                return;
            }
            AdvancedPanelComponent advancedPanelComponent = new AdvancedPanelComponent();
            GridLayoutManager layout = (GridLayoutManager) advancedPanelComponent.getMainPanel().getLayout();
            if (advancedPanelComponents.size() != 0) {
                layout.setMargin(new Insets(30, 0, 0, 0));
            }
            advancedPanelComponents.add(advancedPanelComponent);
            refreshAdvancedPanel();
        });
    }

    private void addActionListenerDeleteButton() {
        deleteJoinButton.addActionListener(tablesField -> {
            if (CollectionUtils.isEmpty(advancedPanelComponents)) {
                return;
            }
            AdvancedPanelComponent advancedPanelComponent = advancedPanelComponents.get(advancedPanelComponents.size() - 1);
            advancedPanel.remove(advancedPanelComponent.getMainPanel());
            advancedPanelComponents.remove(advancedPanelComponents.size() - 1);
            advancedPanel.updateUI();
        });
    }

    /**
     * 刷新高级面板
     */
    private void refreshAdvancedPanel() {
        advancedPanel.removeAll();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(advancedPanelComponents.size() + 1, 1);
        advancedPanel.setLayout(gridLayoutManager);
        for (int i = 0; i < advancedPanelComponents.size(); i++) {
            GridConstraints constraints = new GridConstraints();
            constraints.setFill(GridConstraints.FILL_HORIZONTAL);
            constraints.setAnchor(GridConstraints.ANCHOR_NORTH);
            constraints.setRow((i + 1));
            constraints.setUseParentLayout(true);
            advancedPanel.add(advancedPanelComponents.get(i).getMainPanel(), constraints);
        }
        advancedPanel.updateUI();

    }

    @Override
    protected void doOKAction() {
        if (StringUtils.isEmpty(packageField.getText())) {
            NotificationMessageUtils.notifyError(GenerateCodeContextHelper.getContext().getProject(), "Please fill in the package name");
            return;
        }

        if (StringUtils.isEmpty(pathField.getText())) {
            NotificationMessageUtils.notifyError(GenerateCodeContextHelper.getContext().getProject(), "Please fill in the path name");
            return;
        }

        submit();
        super.doOKAction();
    }

    /**
     * 开始封装代码生成器所需的参数信息
     */
    private void submit() {
        String tableName = (String) tablesField.getSelectedItem();
        DasTable tables = GenerateCodeContextHelper.getContext().getTables(tableName);
        String author = GenerateCodeContextHelper.getContext().getSetting().getAuthor();
        TableInfo tableInfo = new TableInfo(tables, author, packageField.getText(), pathField.getText());
        setTableInfoChild(tableInfo);
        try {
            GenerateCodeContextHelper.getContext().getGenerateProcessor().generate(tableInfo, getTemplateCodes());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void setTableInfoChild(TableInfo tableInfo) {
        if (!advancedButton.isSelected()) {
            return;
        }
        String author = GenerateCodeContextHelper.getContext().getSetting().getAuthor();
        List<TableInfo> childTables = Lists.newArrayList();
        for (AdvancedPanelComponent advancedPanelComponent : advancedPanelComponents) {
            JComboBox tableField = advancedPanelComponent.getTableField();
            DasTable childTable = GenerateCodeContextHelper.getContext().getTables((String) tableField.getSelectedItem());
            TableInfo childTableInfo = new TableInfo(childTable, author, packageField.getText(), pathField.getText());
            childTables.add(childTableInfo);
        }
        tableInfo.setChildTables(childTables);
    }

    private List<String> getTemplateCodes() {
        String templateGroup = (String) commonPanelComponent.getTemplateField().getSelectedItem();
        Component[] components = commonPanelComponent.getTemplatePanel().getComponents();
        List<String> templateCodes = Lists.newArrayList();
        for (Component component : components) {
            if (!(component instanceof JCheckBox)) {
                continue;
            }
            JCheckBox checkBox = (JCheckBox) component;
            if (checkBox.isSelected()) {
                templateCodes.add(checkBox.getText());
            }
        }
        return GenerateCodeContextHelper.getContext().getTemplateCode(templateGroup, templateCodes);
    }


}
