package org.etd.generate.code.plugin.ui.extend.component;

import com.google.common.collect.Maps;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;
import com.intellij.database.util.DasUtil;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Data
public class AdvancedPanelComponent {


    private JPanel mainPanel;

    private JComboBox tableField;

    private JComboBox foreignKey;

    private Map<String, DasTable> tableMap = Maps.newConcurrentMap();

    public AdvancedPanelComponent() {
        init();
    }

    private void init() {
        initTables();
        addActionListeners();
        refreshForeignKey((String) tableField.getSelectedItem());
    }

    private void initTables() {
        tableMap.clear();
        List<DasTable> tables = GenerateCodeContextHelper.getContext().getTables();
        for (DasTable table : tables) {
            tableField.addItem(table.getName());
            tableMap.putIfAbsent(table.getName(), table);
        }
    }

    private void addActionListeners() {
        tableField.addActionListener(comboBox1 -> {
            JComboBox source = (JComboBox) comboBox1.getSource();
            String selectedItem = (String) source.getSelectedItem();
            refreshForeignKey(selectedItem);
        });
    }


    private void refreshForeignKey(String tableName) {
        DasTable dasTable = tableMap.get(tableName);
        if (ObjectUtils.isEmpty(dasTable)) {
            return;
        }
        foreignKey.removeAllItems();
        for (DasColumn column : DasUtil.getColumns(dasTable)) {
            String columnName = column.getName();
            if(DasUtil.isPrimary(column)){
                columnName = columnName +"(PK)";
            }
            foreignKey.addItem(columnName);
        }
    }
}
