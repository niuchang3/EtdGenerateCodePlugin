package org.etd.generate.code.plugin.processor;

import org.apache.commons.lang3.ObjectUtils;
import org.etd.generate.code.plugin.bean.Callback;
import org.etd.generate.code.plugin.bean.TableInfo;
import org.etd.generate.code.plugin.context.GenerateCodeContext;
import org.etd.generate.code.plugin.context.GenerateCodeContextHelper;
import org.etd.generate.code.plugin.processor.extend.FileProcessorImpl;
import org.etd.generate.code.plugin.utils.NotificationMessageUtils;
import org.etd.generate.code.plugin.utils.StringUtil;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class AbstractGenerateProcessor extends FileProcessorImpl implements GenerateProcessor {

    /**
     * 初始化
     */
    protected void beforeGenerate(Map<String, Object> param) {

    }

    protected abstract StringWriter getCode(TableInfo tableInfo, String template);

    /**
     * 将参数填充到上下文中
     *
     * @param key
     * @param value
     */
    protected abstract void putContext(String key, Object value);

    /**
     * 清理上下文
     */
    protected abstract void cleanContext();


    @Override
    public void generate(TableInfo tableInfo, List<String> templeCode) throws CloneNotSupportedException {
        GenerateCodeContext context = GenerateCodeContextHelper.getContext();
        Map<String, Object> param = new HashMap<>(20);
        param.put("stringUtils", StringUtil.getSingleton());
        beforeGenerate(param);

        Callback callback = tableInfo.getCallback();



        for (String template : templeCode) {
            tableInfo.setCallback(callback.copy());
            param.put("tableInfo", tableInfo);

            cleanContext();
            setContext(param);

            StringWriter code = getCode(tableInfo, template);
            if (ObjectUtils.isEmpty(tableInfo.getCallback().getFileName())) {
                NotificationMessageUtils.notifyWarning(context.getProject(), "Callback.FileName Info is null");
                continue;
            }
            write(tableInfo, code);
        }
    }

    private void setContext(Map<String, Object> param) {
        param.forEach((k, v) -> {
            putContext(k, v);
        });

    }


}
