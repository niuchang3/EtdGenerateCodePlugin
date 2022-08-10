package org.etd.generate.code.plugin.bean;

import lombok.Data;

@Data
public class TypeMapping {

    /**
     * 列类型
     */
    private String columnType;
    /**
     * java类型
     */
    private String javaType;
    /**
     * Java短类型
     */
    private String shortJavaType;
}
