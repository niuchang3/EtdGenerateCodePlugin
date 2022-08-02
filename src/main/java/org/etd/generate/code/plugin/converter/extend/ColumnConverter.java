package org.etd.generate.code.plugin.converter.extend;

import com.google.common.collect.Lists;
import com.intellij.database.model.DasColumn;
import com.intellij.database.model.DasTable;
import com.intellij.database.util.DasUtil;
import com.intellij.util.containers.JBIterable;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.converter.AbstractBaseConverter;
import org.etd.generate.code.plugin.converter.BaseConverter;

import java.util.List;

public class ColumnConverter extends AbstractBaseConverter implements BaseConverter<DasTable, List<DasColumn>> {


    private volatile static ColumnConverter columnConverter;

    private ColumnConverter() {

    }

    public static ColumnConverter getSingleton() {
        if (columnConverter == null) {
            synchronized (ColumnConverter.class) {
                if (columnConverter == null) {
                    columnConverter = new ColumnConverter();
                }
            }
        }
        return columnConverter;
    }


    @Override
    public List<DasColumn> convert(DasTable table) {
        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(table);
        if (ObjectUtils.isEmpty(columns)) {
            return Lists.newArrayList();
        }
        return (List<DasColumn>) columns.toList();
    }
}
