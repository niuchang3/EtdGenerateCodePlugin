package org.etd.generate.code.plugin.ui.extend.component;

import com.google.common.collect.Lists;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Data;

import javax.swing.*;
import java.util.ArrayList;

@Data
public class CommonPanelComponent {

    private JPanel mainPanel;
    private JComboBox templateField;
    private JPanel templatePanel;

    private ArrayList<String> checkList = Lists.newArrayList("111.vm", "222.vm", "333.vm", "4444.vm", "555.vm");

    public CommonPanelComponent() {
        init();
    }

    private void init() {
        initTemplatePanel();
        initTemplate();
        initEvent();
    }

    private void initTemplatePanel() {
        int row = checkList.size() / 3;
        if (checkList.size() % 3 > 0) {
            row++;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(row, 3);
        templatePanel.setLayout(gridLayoutManager);
        refreshTemplatePanel();
    }

    private void initEvent() {
        templateField.addActionListener(templateGroup -> {
            refreshTemplatePanel();
        });
    }

    private void initTemplate() {
        templateField.addItem("test1");
        templateField.addItem("test2");
        templateField.addItem("test3");
    }

    private void refreshTemplatePanel() {
        templatePanel.removeAll();
        int row = 0;
        for (int i = 0; i < checkList.size(); i++) {
            GridConstraints gridConstraints = new GridConstraints();
            gridConstraints.setVSizePolicy(0);
            gridConstraints.setHSizePolicy(3);
            gridConstraints.setFill(GridConstraints.FILL_HORIZONTAL);
            gridConstraints.setAnchor(GridConstraints.ANCHOR_WEST);
            gridConstraints.setRow(row);
            gridConstraints.setRowSpan(1);
            gridConstraints.setColSpan(1);
            gridConstraints.setColumn(i % 3);
            gridConstraints.setUseParentLayout(true);
            JCheckBox jCheckBox = new JCheckBox(checkList.get(i));

            templatePanel.add(jCheckBox, gridConstraints, i);
            if (i != 0 && (i + 1) % 3 == 0) {
                row++;
            }
        }
        this.mainPanel.updateUI();
    }

}
