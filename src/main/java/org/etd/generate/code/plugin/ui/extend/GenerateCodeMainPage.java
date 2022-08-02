package org.etd.generate.code.plugin.ui.extend;

import com.intellij.database.model.DasTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.context.GenerateCodeContext;
import org.etd.generate.code.plugin.listener.PackageButtonActionListener;
import org.etd.generate.code.plugin.listener.PathButtonActionListener;
import org.etd.generate.code.plugin.tool.NotificationMessageUtils;
import org.etd.generate.code.plugin.ui.BaseDialogWrapper;
import org.etd.generate.code.plugin.ui.extend.component.AdvancedPanelComponent;
import org.etd.generate.code.plugin.ui.extend.component.CommonPanelComponent;
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
        super(event, false, "EtdGenerateCode Title Info", 500, 300);
        initDialog();
    }


    protected void initDialog() {
        commonPanel.add(commonPanelComponent.getMainPanel(), getGridConstraints());
        initTablesField();
        // 填充默认的代码生成路径
        pathField.setText(GenerateCodeContextHelper.getContext().getProject().getBasePath());
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


}
