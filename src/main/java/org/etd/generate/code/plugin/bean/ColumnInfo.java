package org.etd.generate.code.plugin.bean;

import com.intellij.database.model.DasColumn;
import com.intellij.database.util.DasUtil;
import lombok.Data;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.converter.BaseConverter;
import org.etd.generate.code.plugin.converter.extend.TypeMappingConverter;
import org.etd.generate.code.plugin.utils.ToHumpUtil;

import java.io.Serializable;

/**
 * 列信息
 */
@Data
public class ColumnInfo implements Serializable {
    /**
     * 原始列信息
     */
    private DasColumn obj;
    /**
     * 是否为主键列
     */
    private Boolean isPk;
    /**
     * 列名称
     */
    private String name;

    /**
     * 注释
     */
    private String comment;
    /**
     * 数据类型
     */
    private String type;
    /**
     *
     */
    private String jdbcType;
    /**
     * 短类型
     */
    private String shortJavaType;

    public ColumnInfo(DasColumn obj) {
        this.obj = obj;
        this.isPk = DasUtil.isPrimary(obj);
        this.name = ToHumpUtil.toHump(obj.getName());
        this.comment = obj.getComment();
        this.type = obj.getDataType().typeName;
        BaseConverter converter = GenerateCodeContextHelper.getContext().getConverter(TypeMappingConverter.class);
        String convertType = (String) converter.convert(obj);
        this.jdbcType = convertType;
        String[] split = this.jdbcType.split("\\.");
        this.shortJavaType = split[split.length - 1];
    }
}
