package org.etd.generate.code.plugin.bean;

import lombok.Data;
import org.etd.generate.code.plugin.constants.MatchType;

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
     * 匹配方式
     */
    private MatchType matchType;
}
