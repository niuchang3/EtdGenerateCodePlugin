package org.etd.generate.code.plugin.converter.extend;

import com.google.common.collect.Lists;
import com.intellij.database.model.DasTable;
import com.intellij.database.model.basic.BasicElement;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.constants.BaseConstants;
import org.etd.generate.code.plugin.converter.AbstractBaseConverter;
import org.etd.generate.code.plugin.converter.BaseConverter;

import java.util.List;

public class TableConverter extends AbstractBaseConverter implements BaseConverter<AnActionEvent, List<DasTable>> {

    private volatile static TableConverter tableConverter;

    private TableConverter() {

    }

    public static TableConverter getSingleton() {
        if (tableConverter == null) {
            synchronized (TableConverter.class) {
                if (tableConverter == null) {
                    tableConverter = new TableConverter();
                }
            }
        }
        return tableConverter;
    }

    @Override
    public List<DasTable> convert(AnActionEvent actionEvent) {

        BasicElement[] basicElements = getBasicElementArray(actionEvent);
        return elementToList(basicElements);
    }


    private BasicElement[] getBasicElementArray(AnActionEvent actionEvent) {
        return actionEvent.getData(BaseConstants.DATABASE_ELEMENTS);
    }

    private List<DasTable> elementToList(BasicElement[] elements) {
        if (ObjectUtils.isEmpty(elements)) {
            return Lists.newArrayList();
        }
        List<DasTable> tables = Lists.newArrayList();
        for (BasicElement element : elements) {
            tables.add((DasTable) element);
        }
        return tables;
    }


}
