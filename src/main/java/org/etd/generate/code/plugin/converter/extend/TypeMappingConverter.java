package org.etd.generate.code.plugin.converter.extend;

import com.intellij.database.model.DasColumn;
import org.etd.generate.code.plugin.bean.TypeMapping;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.converter.AbstractBaseConverter;
import org.etd.generate.code.plugin.converter.BaseConverter;

import java.util.List;

/**
 * @author Niuchang
 */
public class TypeMappingConverter extends AbstractBaseConverter implements BaseConverter<DasColumn, String> {

    private volatile static TypeMappingConverter tableConverter;

    private TypeMappingConverter() {

    }

    public static TypeMappingConverter getSingleton() {
        if (tableConverter == null) {
            synchronized (TableConverter.class) {
                if (tableConverter == null) {
                    tableConverter = new TypeMappingConverter();
                }
            }
        }
        return tableConverter;
    }

    /**
     * 默认
     *
     * @param entity
     * @return
     */
    @Override
    public String convert(DasColumn entity) {
        List<TypeMapping> typeMappings = GenerateCodeContextHelper.getContext().getSetting().getTypeMappings();
        for (TypeMapping typeMapping : typeMappings) {
            String typeName = entity.getDataType().typeName;
            if (typeName.equalsIgnoreCase(typeMapping.getColumnType())) {
                return typeMapping.getJavaType();
            }

        }
        return null;
    }

}
