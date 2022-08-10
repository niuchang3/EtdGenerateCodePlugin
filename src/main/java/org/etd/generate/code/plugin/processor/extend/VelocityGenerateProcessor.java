package org.etd.generate.code.plugin.processor.extend;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.etd.generate.code.plugin.bean.TableInfo;
import org.etd.generate.code.plugin.processor.AbstractGenerateProcessor;
import org.etd.generate.code.plugin.processor.GenerateProcessor;

import java.io.StringWriter;

/**
 * 默认的代码生成处理器
 */
public class VelocityGenerateProcessor extends AbstractGenerateProcessor implements GenerateProcessor {

    private VelocityEngine velocityEngine = new VelocityEngine();

    private VelocityContext context = new VelocityContext();


    @Override
    protected StringWriter getCode(TableInfo tableInfo, String template) {
        StringWriter stringWriter = new StringWriter();
        velocityEngine.evaluate(context, stringWriter, "Velocity Generate Processor", template);
        return stringWriter;
    }

    @Override
    protected void putContext(String key, Object value) {
        context.put(key, value);
    }

    @Override
    protected void cleanContext() {
        context = new VelocityContext();
    }

}
