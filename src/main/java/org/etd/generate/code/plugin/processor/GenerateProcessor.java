package org.etd.generate.code.plugin.processor;

import org.etd.generate.code.plugin.bean.TableInfo;

import java.util.List;

public interface GenerateProcessor {

    void generate(TableInfo tableInfo, List<String> templeCode) throws CloneNotSupportedException;
}
