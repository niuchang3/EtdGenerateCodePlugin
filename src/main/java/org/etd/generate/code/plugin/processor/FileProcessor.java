package org.etd.generate.code.plugin.processor;

import org.etd.generate.code.plugin.bean.TableInfo;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 文件处理器，将模板信息持久化
 */
public interface FileProcessor {


    void write(TableInfo tableInfo,StringWriter stringWriter) throws IOException;

}
