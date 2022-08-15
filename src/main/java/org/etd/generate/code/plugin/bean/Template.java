package org.etd.generate.code.plugin.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模板
 *
 * @author 牛昌
 */
@EqualsAndHashCode
@Data
public class Template {

    public Template() {
    }

    public Template(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private String name;

    private String code;
}
