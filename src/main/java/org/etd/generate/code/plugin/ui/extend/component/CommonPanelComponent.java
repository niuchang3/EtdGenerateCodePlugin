package org.etd.generate.code.plugin.ui.extend.component;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.bean.Settings;
import org.etd.generate.code.plugin.bean.Template;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Data
public class CommonPanelComponent {

    private JPanel mainPanel;
    private JComboBox templateField;
    private JPanel templatePanel;


    public CommonPanelComponent() {
        init();
    }

    private void init() {
        initTemplatePanel();
        initTemplate();
        initEvent();
    }

    private void initTemplatePanel() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(6, 3);
        templatePanel.setLayout(gridLayoutManager);
    }

    private void initEvent() {
        templateField.addActionListener(templateGroup -> {
            JComboBox source = (JComboBox) templateGroup.getSource();
            refreshTemplatePanel((String) source.getSelectedItem());
        });
    }

    private void initTemplate() {
        Settings setting = GenerateCodeContextHelper.getContext().getSetting();
        setting.getTemplates().forEach((k, v) -> {
            templateField.addItem(k);
        });
        refreshTemplatePanel((String) templateField.getSelectedItem());
    }

    private void refreshTemplatePanel(String templateName) {
        templatePanel.removeAll();
        int row = 0;
        Settings setting = GenerateCodeContextHelper.getContext().getSetting();
        Map<String, List<Template>> tamplates = setting.getTemplates();
        if (ObjectUtils.isEmpty(tamplates)) {
            return;
        }
        List<Template> templateList = tamplates.get(templateName);
        if (CollectionUtils.isEmpty(templateList)) {
//            NotificationMessageUtils.notifyError(GenerateCodeContextHelper.getContext().getProject(), "template is null");
            return;
        }

        for (int i = 0; i < templateList.size(); i++) {
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
            JCheckBox jCheckBox = new JCheckBox(templateList.get(i).getName());

            templatePanel.add(jCheckBox, gridConstraints, i);
            if (i != 0 && (i + 1) % 3 == 0) {
                row++;
            }
        }
        this.mainPanel.updateUI();
    }

}
